
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.field-adornments.views
    (:require [mid-fruits.candy                               :refer [param]]
              [mid-fruits.vector                              :as vector]
              [x.app-components.api                           :as components]
              [x.app-core.api                                 :as a]
              [x.app-elements.focusable-elements.side-effects :as focusable-elements.side-effects]
              [x.app-environment.api                          :as environment]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-elements.focusable-elements.side-effects
(def focus-element! x.app-elements.focusable-elements.side-effects/focus-element!)



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- adornment-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) adornment-props
  ;  {:icon (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:icon-family (keyword)}
  [{:keys [icon] :as adornment-props}]
  (merge (if icon {:icon-family :material-icons-filled})
         (param adornment-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-adornment
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (map) adornment-props
  ;  {:icon (keyword)(opt)
  ;   :icon-family (keyword)(opt)
  ;    :material-icons-filled, :material-icons-outlined
  ;    Default: :material-icons-filled
  ;   :label (string)(opt)
  ;   :on-click (metamorphic-event)
  ;   :tab-indexed? (boolean)(opt)
  ;    False érték esetén az adornment gomb nem indexelődik tabolható elemként.
  ;    Default: true
  ;   :tooltip (metamorphic-content)(opt)}
  [field-id _ {:keys [icon icon-family label on-click tab-indexed? tooltip]}]
  (let [; BUG#2105
        ;  A *-field elemhez adott field-adornment-button gombon történő on-mouse-down esemény
        ;  a mező on-blur eseményének triggerelésével jár, ami a mezőhöz esetlegesen használt surface
        ;  felület React-fából történő lecsatolását okozná.
        button-attributes (merge {:on-mouse-down #(do (.preventDefault %))
                                  :on-mouse-up   #(do (a/dispatch on-click)
                                                      (environment/blur-element!))
                                  :title            (components/content tooltip)}
                                 (if (false? tab-indexed?) {:tab-index "-1"})
                                 (if icon {:data-icon-family icon-family}))]
       (cond icon  [:button.x-field-adornments--button-adornment button-attributes icon]
             label [:button.x-field-adornments--button-adornment button-attributes label])))

(defn static-adornment
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (map) adornment-props
  ;  {:icon (keyword)(opt)
  ;   :icon-family (keyword)(opt)
  ;    :material-icons-filled, :material-icons-outlined
  ;    Default: :material-icons-filled
  ;   :label (string)(opt)}
  [_ _ {:keys [icon icon-family label]}]
  (cond icon  [:div.x-field-adornments--static-adornment {:data-icon-family icon-family} icon]
        label [:div.x-field-adornments--static-adornment label]))

(defn field-adornment
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (map) adornment-props
  ;  {:on-click (metamorphic-event)(opt)}
  [field-id field-props {:keys [on-click] :as adornment-props}]
  (if on-click [button-adornment field-id field-props adornment-props]
               [static-adornment field-id field-props adornment-props]))

(defn field-end-adornments
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:end-adornments (maps in vector)(opt)}
  [field-id {:keys [end-adornments] :as field-props}]
  (if (vector/nonempty? end-adornments)
      (letfn [(f [adornments adornment-props]
                 (let [adornment-props (adornment-props-prototype adornment-props)]
                      (conj adornments [field-adornment field-id field-props adornment-props])))]
             (reduce f [:div.x-field-adornments] end-adornments))
      [:div.x-field-adornments--placeholder {:on-mouse-down #(.preventDefault %)
                                             :on-mouse-up   #(focus-element! field-id)}]))

(defn field-start-adornments
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:start-adornments (maps in vector)(opt)}
  [field-id {:keys [start-adornments] :as field-props}]
  (if (vector/nonempty? start-adornments)
      (letfn [(f [adornments adornment-props]
                 (let [adornment-props (adornment-props-prototype adornment-props)]
                      (conj adornments [field-adornment field-id field-props adornment-props])))]
             (reduce f [:div.x-field-adornments] start-adornments))
      [:div.x-field-adornments--placeholder {:on-mouse-down #(.preventDefault %)
                                             :on-mouse-up   #(focus-element! field-id)}]))
