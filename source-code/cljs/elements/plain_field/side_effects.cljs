
(ns elements.plain-field.side-effects
    (:require [elements.plain-field.helpers :as plain-field.helpers]
              [elements.plain-field.state   :as plain-field.state]
              [hiccup.api                   :as hiccup]
              [re-frame.api                 :as r]
              [x.environment.api            :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn empty-field!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  [field-id]
  (plain-field.helpers/set-field-content! field-id ""))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn focus-field!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id _]
  (let [field-input-id (hiccup/value field-id "input")]
       (x.environment/move-caret-to-end! field-input-id)
       (x.environment/focus-element!     field-input-id)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn show-surface!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  [field-id]
  ; The 'show-surface!' function stores which field displays its surface currently.
  ; Only one field allowed to displays its surface at a time, therefore this
  ; function overwrites the previously set value.
  ;
  ; BUG#6071
  ; Be sure you only display surfaces for focused fields!
  ; E.g.: If an event applies the 'show-surface!' function and that event is fired
  ;       by the on-type-ended trigger, the user has enough time to leave the field,
  ;       maybe steps into another field before the on-type-ended trigger shows
  ;       the surface!
  (reset! plain-field.state/VISIBLE-SURFACE field-id))

(defn hide-surface!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  [_]
  (reset! plain-field.state/VISIBLE-SURFACE nil))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :elements.plain-field/empty-field! empty-field!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :elements.plain-field/focus-field! focus-field!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :elements.plain-field/show-surface! show-surface!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :elements.plain-field/hide-surface! hide-surface!)
