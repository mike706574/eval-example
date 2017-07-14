(ns eval.views
  (:require [reagent.core :as r]
            [re-frame.core :as rf]))

(defn pretty [form] (with-out-str (cljs.pprint/pprint form)))

(defn error []
  (let [error @(rf/subscribe [:error])]
    [:div
     [:h1 "Error " (eval/error-context error)]
     (when-let [event (eval/error-event error)]
       [:event
        [:h5 "Event"]
        [:pre (pretty event)]])
     [:h5 "Data"]
     [:pre (pretty (eval/error-data error))]]))

(defn editor-did-mount []
  (fn [this]
    (let [cm (.fromTextArea  js/CodeMirror
                             (r/dom-node this)
                             #js {:mode "clojure"
                                  :lineNumbers true})]
      (.on cm "change" #(rf/dispatch [:change-code (.getValue %)])))))

(defn editor []
  (r/create-class
   {:render (fn [] [:textarea
                    {:default-value ""
                     :auto-complete "off"}])
    :component-did-mount (editor-did-mount)}))

(defn ok
  []
  [:div
   [editor]
   [:br]
   [:button
    {:type "button"
     :on-click #(rf/dispatch [:eval-code])}
    "Eval"]
   [:h5 "Output"]
   [:textarea
    {:readOnly true
     :value (pretty @(rf/subscribe [:output]))}]
   [:h5 "Log"]
   [:textarea
    {:readOnly true
     :value @(rf/subscribe [:log])}]])

(defn app []
  (let [status @(rf/subscribe [:status])]
    (case status
      :ok [ok]
      :booting [:p "Booting..."]
      :error [error]
      [:p "Error!"])))
