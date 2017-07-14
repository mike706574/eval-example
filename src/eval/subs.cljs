(ns eval.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :status
 (fn [db _]
   (:eval/status db)))

(rf/reg-sub
 :error
 (fn [db _]
   (select-keys db [:eval/error-context
                    :eval/error-data
                    :eval/error-event])))

(rf/reg-sub
 :code
 (fn [db _]
   (:eval/code db)))

(rf/reg-sub
 :output
 (fn [db _]
   (:eval/output db)))

(rf/reg-sub
 :log
 (fn [db _]
   (:eval/log db)))
