
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.progress-screen.views
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [screen-locked? @(a/subscribe [:ui/screen-locked?])]
          [:div#x-app-progress-screen
            (str (mid-fruits.vector/remove-item-once ["a"] "a")
                 "xxxx")]))
