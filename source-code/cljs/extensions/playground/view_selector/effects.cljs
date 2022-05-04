
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.playground.view-selector.effects
    (:require [extensions.playground.view-selector.views :as view-selector.views]
              [x.app-core.api                            :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :playground.view-selector/load-selector!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:dispatch-n [[:ui/set-title! :playground]
                [:playground/initialize!]
                [:playground.view-selector/render-selector!]]})

(a/reg-event-fx
  :playground.view-selector/render-selector!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/render-surface! :playground/view
                       {:content #'view-selector.views/view}])
