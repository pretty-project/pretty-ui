
(ns pretty-inputs.input.side-effects
    (:require [pretty-inputs.input.state :as input.state]
              [re-frame.api              :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn mark-input-as-focused!
  ; @ignore
  ;
  ; @param (keyword) input-id
  [input-id]
  (swap! input.state/FOCUSED-INPUTS assoc input-id true))

(defn unmark-input-as-focused!
  ; @ignore
  ;
  ; @param (keyword) input-id
  [input-id]
  (swap! input.state/FOCUSED-INPUTS dissoc input-id))

(defn mark-input-as-visited!
  ; @ignore
  ;
  ; @param (keyword) input-id
  [input-id]
  (swap! input.state/VISITED-INPUTS assoc input-id true))

(defn unmark-input-as-visited!
  ; @ignore
  ;
  ; @param (keyword) input-id
  [input-id]
  (swap! input.state/VISITED-INPUTS dissoc input-id))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-initial-value!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  [input-id {:keys [initial-value on-change-f value]}]
  (if (-> value nil?)
      (-> initial-value on-change-f)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn render-popup!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  [input-id _]
  (mark-input-as-focused! input-id)
  (reset! input.state/RENDERED-POPUP input-id))

(defn update-popup!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  [input-id input-props]
  (render-popup! input-id input-props))

(defn close-popup!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  [_ _]
  (reset! input.state/RENDERED-POPUP nil))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @param (keyword) input-id
(r/reg-fx :pretty-inputs.input/mark-input-as-focused! mark-input-as-focused!)

; @ignore
;
; @param (keyword) input-id
(r/reg-fx :pretty-inputs.input/unmark-input-as-focused! unmark-input-as-focused!)

; @ignore
;
; @param (keyword) input-id
(r/reg-fx :pretty-inputs.input/mark-input-as-visited! mark-input-as-visited!)

; @ignore
;
; @param (keyword) input-id
(r/reg-fx :pretty-inputs.input/unmark-input-as-visited! unmark-input-as-visited!)

; @ignore
;
; @param (keyword) input-id
; @param (map) input-props
(r/reg-fx :pretty-inputs.input/render-popup! render-popup!)

; @ignore
;
; @param (keyword) input-id
; @param (map) input-props
(r/reg-fx :pretty-inputs.input/update-popup! update-popup!)

; @ignore
;
; @param (keyword) input-id
; @param (map) input-props
(r/reg-fx :pretty-inputs.input/close-popup! close-popup!)
