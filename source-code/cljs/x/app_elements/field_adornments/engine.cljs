
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.field-adornments.engine
    (:require [x.app-components.api                           :as components]
              [x.app-core.api                                 :as a]
              [x.app-elements.focusable-elements.side-effects :as focusable-elements.side-effects]
              [x.app-environment.api                          :as environment]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-elements.focusable-elements.side-effects
(def focus-element! x.app-elements.focusable-elements.side-effects/focus-element!)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-adornment-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (map) adornment-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :icon (keyword)(opt)
  ;   :icon-family (keyword)(opt)
  ;    :material-icons-filled, :material-icons-outlined
  ;    Default: :material-icons-filled
  ;   :on-click (metamorphic-event)
  ;   :tab-indexed? (boolean)(opt)
  ;    Default: true
  ;   :tooltip (metamorphic-content)(opt)}
  ;
  ; @return (map)
  ;  {}
  [_ _ {:keys [disabled? icon icon-family on-click tab-indexed? tooltip]}]
  ; BUG#2105
  ; A *-field elemhez adott field-adornment-button gombon történő on-mouse-down esemény
  ; a mező on-blur eseményének triggerelésével jár, ami a mezőhöz esetlegesen használt surface
  ; felület React-fából történő lecsatolását okozná.
  (merge {:title          (components/content tooltip)
          :on-mouse-down #(.preventDefault %)}
         (if     icon         {:data-icon-family icon-family})
         (if     disabled?    {:disabled   "1" :data-disabled true})
         (if-not tab-indexed? {:tab-index "-1"})
         (if-not disabled?    {:on-mouse-up #(do (a/dispatch on-click)
                                                 (environment/blur-element!))})))

(defn adornment-placeholder-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ;  {}
  [field-id _]
  {:on-mouse-down #(.preventDefault %)
   :on-mouse-up   #(focus-element! field-id)})
