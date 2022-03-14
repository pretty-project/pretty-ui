
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.progress-bar.views
    (:require [mid-fruits.css :as css]
              [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- progress-bar-process-progress
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [process-failured? @(a/subscribe [:ui/process-failured?])
        process-progress  @(a/subscribe [:ui/get-process-progress])]
       [:div#x-app-progress-bar--process-progress {:style {:height (case process-progress 0 "0" "6px")
                                                           :width  (css/percent process-progress)}
                                                   :data-failured  (boolean     process-failured?)}]))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  ; Ahhoz, hogy a [:ui/fake-process! ...] esemény használatával beállított hamis folyamatjelző állapota
  ; átmenetesen jelenjen meg (CSS transition), az elemnek már a DOM-fában kell lennie az érték beállításakor!
  [:div#x-app-progress-bar [progress-bar-process-progress]])
