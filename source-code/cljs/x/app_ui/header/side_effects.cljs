
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.header.side-effects
    (:require [x.app-core.api        :as a]
              [x.app-ui.header.state :as header.state]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-header-title!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (metamorphic-content) header-title
  [header-title]
  (reset! header.state/HEADER-TITLE header-title))

(defn remove-header-title!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (reset! header.state/HEADER-TITLE nil))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:ui/set-header-title! "My title"]
(a/reg-fx :ui/set-header-title! set-header-title!)

; @usage
;  [:ui/remove-header-title!]
(a/reg-fx :ui/remove-header-title! remove-header-title!)
