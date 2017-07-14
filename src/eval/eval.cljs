(ns eval.eval
  (:require [cljs.js :refer [empty-state eval js-eval]]
            [cljs.tools.reader :refer [read-string]]))

(defn eval-str [s]
  (eval (empty-state)
        (read-string s)
        {:eval js-eval
         :source-map true
         :context :expr}
        identity))

(defn eval-with-log [s]
  (let [log (js/goog.string.StringBuffer.)]
    (binding [cljs.core/*print-newline* true
              cljs.core/*print-fn* (fn [s] (.append log s))]
      (assoc (eval-str s) :log (str log)))))
