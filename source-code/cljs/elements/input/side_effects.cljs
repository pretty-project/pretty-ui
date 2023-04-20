
(ns elements.input.side-effects
    (:require [elements.input.state :as input.state]
              [re-frame.api         :as r]))

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
(r/reg-fx :elements.input/mark-input-as-focused! mark-input-as-focused!)

; @ignore
(r/reg-fx :elements.input/unmark-input-as-focused! unmark-input-as-focused!)

; @ignore
(r/reg-fx :elements.input/mark-input-as-visited! mark-input-as-visited!)

; @ignore
(r/reg-fx :elements.input/unmark-input-as-visited! unmark-input-as-visited!)

; @ignore
(r/reg-fx :elements.input/render-popup! render-popup!)

; @ignore
(r/reg-fx :elements.input/update-popup! update-popup!)

; @ignore
(r/reg-fx :elements.input/close-popup! close-popup!)
