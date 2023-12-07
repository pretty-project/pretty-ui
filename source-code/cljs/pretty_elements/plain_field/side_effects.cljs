
(ns pretty-elements.plain-field.side-effects
    (:require [dom.api                           :as dom]
              [fruits.hiccup.api                 :as hiccup]
              [keypress-handler.api              :as keypress-handler]
              [pretty-elements.plain-field.state :as plain-field.state]
              [re-frame.api                      :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-field-content!
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (string) field-content
  [field-id field-content]
  ; HACK#9910
  ;
  ; BUG#3401
  ; The 'field-content' has to be converted to string type!
  ; It may occur, that a non-seqable type (e.g., integer) being written into
  ; the field and the empty? function throws an error in case of taking
  ; a non-seqable value as its argument.
  (swap! plain-field.state/FIELD-CONTENTS assoc field-id (str field-content)))

(defn set-field-output!
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (string) field-content
  [field-id field-content]
  ; HACK#9910
  (swap! plain-field.state/FIELD-OUTPUTS assoc field-id field-content))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn empty-field!
  ; @ignore
  ;
  ; @param (keyword) field-id
  [field-id]
  (set-field-content! field-id ""))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn focus-field!
  ; @ignore
  ;
  ; @param (keyword) field-id
  [field-id]
  (let [field-input-id (hiccup/value field-id "input")]
       (when-let [field-input-element (dom/get-element-by-id field-input-id)]
                 (dom/move-caret-to-end! field-input-element)
                 (dom/focus-element!     field-input-element))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn show-surface!
  ; @ignore
  ;
  ; @param (keyword) field-id
  [field-id]
  ; The 'show-surface!' function stores which field displays its surface currently.
  ; Only one field allowed to displays its surface at a time,
  ; Therefore, this function overwrites the previously set value.
  ;
  ; BUG#6071
  ; Ensure that you only display surfaces for focused fields!
  ; E.g.: If an event applies the 'show-surface!' function and that event is fired
  ;       by the :on-type-ended trigger, the user has enough time to leave the field,
  ;       maybe steps into another field before the :on-type-ended trigger displays
  ;       the surface!
  (reset! plain-field.state/VISIBLE-SURFACE field-id))

(defn hide-surface!
  ; @ignore
  ;
  ; @param (keyword) field-id
  [_]
  (reset! plain-field.state/VISIBLE-SURFACE nil))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-type-mode!
  ; @ignore
  ;
  ; @param (keyword) field-id
  [_]
  (keypress-handler/set-type-mode!))

(defn quit-type-mode!
  ; @ignore
  ;
  ; @param (keyword) field-id
  [_]
  (keypress-handler/quit-type-mode!))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @param (keyword) field-id
(r/reg-fx :pretty-elements.plain-field/empty-field! empty-field!)

; @ignore
;
; @param (keyword) field-id
(r/reg-fx :pretty-elements.plain-field/focus-field! focus-field!)

; @ignore
;
; @param (keyword) field-id
(r/reg-fx :pretty-elements.plain-field/show-surface! show-surface!)

; @ignore
;
; @param (keyword) field-id
(r/reg-fx :pretty-elements.plain-field/hide-surface! hide-surface!)

; @ignore
;
; @param (keyword) field-id
(r/reg-fx :pretty-elements.plain-field/set-type-mode! set-type-mode!)

; @ignore
;
; @param (keyword) field-id
(r/reg-fx :pretty-elements.plain-field/quit-type-mode! quit-type-mode!)
