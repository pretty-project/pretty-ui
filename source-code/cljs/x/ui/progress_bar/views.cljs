
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.progress-bar.views
    (:require [css.api      :as css]
              [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- progress-bar-process-progress
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [process-failured? @(r/subscribe [:x.ui/process-failured?])
        process-progress  @(r/subscribe [:x.ui/get-process-progress])]
       [:div#x-app-progress-bar--process-progress {:style {:height (case process-progress 0 "0" "6px")
                                                           :width  (css/percent process-progress)}
                                                   :data-failured  (boolean     process-failured?)}]))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  ; Ahhoz, hogy a [:x.ui/fake-process! ...] esemény használatával beállított hamis folyamatjelző állapota
  ; átmenetesen jelenjen meg (CSS transition), az elemnek már a DOM-fában kell lennie az érték beállításakor!
  [:div#x-app-progress-bar [progress-bar-process-progress]])
