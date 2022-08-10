
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns settings.view-selector.effects
    (:require [settings.view-selector.views :as view-selector.views]
              [x.app-core.api               :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :settings.view-selector/load-selector!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:settings.view-selector/render-selector!])

(a/reg-event-fx
  :settings.view-selector/render-selector!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/render-surface! :settings.view-selector/view
                       {:content #'view-selector.views/view}])
