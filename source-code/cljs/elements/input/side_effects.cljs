
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

; @ignore
(r/reg-fx :elements.input/mark-input-as-focused! mark-input-as-focused!)

; @ignore
(r/reg-fx :elements.input/unmark-input-as-focused! unmark-input-as-focused!)

; @ignore
(r/reg-fx :elements.input/mark-input-as-visited! mark-input-as-visited!)

; @ignore
(r/reg-fx :elements.input/unmark-input-as-visited! unmark-input-as-visited!)
