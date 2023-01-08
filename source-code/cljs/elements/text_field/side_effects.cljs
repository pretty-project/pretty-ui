
(ns elements.text-field.side-effects
    (:require [elements.text-field.helpers :as text-field.helpers]
              [elements.text-field.state   :as text-field.state]
              [hiccup.api                  :as hiccup]
              [re-frame.api                :as r]
              [string.api                  :as string]
              [x.environment.api           :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-initial-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:initial-value (string)
  ;  :field-content-f (function)}
  [field-id {:keys [initial-value field-content-f]}]
  (let [field-content (field-content-f initial-value)]
       (text-field.helpers/set-field-content! field-id field-content)))

(defn use-stored-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:field-content-f (function)}
  ; @param (string) stored-value
  [field-id {:keys [field-content-f]} stored-value]
  (let [field-content (field-content-f stored-value)]
       (text-field.helpers/set-field-content! field-id field-content)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn empty-field!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  [field-id]
  (text-field.helpers/set-field-content! field-id string/EMPTY-STRING))

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
  (reset! text-field.state/VISIBLE-SURFACE field-id))

(defn hide-surface!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  [_]
  (reset! text-field.state/VISIBLE-SURFACE nil))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :elements.text-field/use-initial-value! use-initial-value!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :elements.text-field/use-stored-value! use-stored-value!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :elements.text-field/empty-field! empty-field!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :elements.text-field/focus-field! focus-field!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :elements.text-field/show-surface! show-surface!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :elements.text-field/hide-surface! hide-surface!)
