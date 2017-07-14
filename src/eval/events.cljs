(ns eval.events
  (:require [eval.eval :as eval]
            [clojure.spec.alpha :as s]
            [re-frame.core :as rf]
            [re-frame.interceptor :refer [->interceptor
                                          assoc-effect
                                          get-effect
                                          get-coeffect]]
            [taoensso.timbre :as log]))

(defmulti db-status :eval/status)

(s/def :eval/code string?)
(s/def :eval/log string?)

(defmethod db-status :ok [_]
  (s/keys :req [:eval/code
                :eval/output
                :eval/log]))

(s/def :eval/error map?)
(s/def :eval/error-context string?)

(defmethod db-status :error [_]
  (s/keys :req [:eval/status
                :eval/error-context
                :eval/error-data]
          :opt [:eval/error-event]))

(s/def :eval/db (s/multi-spec db-status :eval/status))

(defn spec-interceptor
  [spec f]
  (->interceptor
   :id :spec
   :after (fn spec-validation
            [context]
            (let [event (get-coeffect context :event)
                  db (or (get-effect context :db)
                         (get-coeffect context :db))]
              (if (s/valid? spec db)
                context
                (->> (f db event (s/explain-data spec db))
                     (assoc-effect context :db)))))))

(defn handle-invalid-db
  [db event data]
  (merge db {:eval/status :error
             :eval/error-context (str " validating db after " (first event))
             :eval/error-event event
             :eval/error-data data}))

(def my-spec-interceptor (spec-interceptor :eval/db handle-invalid-db))

(def interceptors [my-spec-interceptor rf/debug rf/trim-v])

(defn reg-event-db [k f] (rf/reg-event-db k interceptors f))

(defn boot
  [db _]
  {:eval/status :ok
   :eval/code ""
   :eval/output nil
   :eval/log ""})

(defn change-code
  [db [code]]
  (assoc db :eval/code code))

(defn eval-code
  [db event]
  (let [{:keys [value log] :as response} (-> db
                                             :eval/code
                                             eval/eval-with-log)]
    (assoc db :eval/output value :eval/log log)))

(reg-event-db :boot boot)
(reg-event-db :change-code change-code)
(reg-event-db :eval-code eval-code)
