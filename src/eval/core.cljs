(ns eval.core
  (:require [eval.events]
            [eval.subs]
            [eval.views]
            [devtools.core :as devtools]
            [goog.events :as events]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [taoensso.timbre :as log]))

(devtools/install!)
(enable-console-print!)
(log/set-level! :debug)

(defn ^:export run
  []
  (rf/dispatch-sync [:boot])
  (r/render [eval.views/app] (js/document.getElementById "app")))
