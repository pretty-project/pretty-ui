
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-editor.sample
    (:require [engines.item-editor.api :as item-editor]
              [re-frame.api            :as r]))



;; -- Szerver-oldali beállítás ------------------------------------------------
;; ----------------------------------------------------------------------------

; Az engine beállításához mindenképpen szükséges a szerver-oldali
; [:item-editor/init-editor! ...] eseményt használni!



;; -- Az engine használata alapbeállításokkal ----------------------------------
;; ----------------------------------------------------------------------------

(defn my-editor
  []
  [item-editor/body :my-editor {:form-element [:div "My form"]}])



;; -- Az {:auto-title? true} beállítás használata -----------------------------
;; ----------------------------------------------------------------------------

; ...
; A body komponens {:label-key ...} paraméterét is szükséges megadni
; az {:auto-title? true} beállítás használatához!



;; -- A ".../create" útvonal használata ---------------------------------------
;; ----------------------------------------------------------------------------

; ...


;; -- Az {:default-item {...}} tulajdonság használata -------------------------
;; ----------------------------------------------------------------------------

; ...



;; -- Az {:initial-item {...}} tulajdonság használata -------------------------
;; ----------------------------------------------------------------------------

; ...



;; -- Az [:item-editor/edit-item! "..."] esemény hanszálata -------------------
;; ----------------------------------------------------------------------------

; ...
(r/reg-event-fx :edit-my-item!
  [:item-editor/edit-item! :my-editor "my-item"])



;; -- Az {:item-id "..."} paraméter használata --------------------------------
;; ----------------------------------------------------------------------------

; A body komponens {:item-id "..."} paraméterének értékét használja az aktuálisan
; szerkesztett elem azonosítójaként, ...
; ... amikor az aktuális útvonalból nem származtatható az :item-id útvonal-paraméter.
; ... az engine NEM útvonal-vezérelt.



;; -- Pathom lekérés használata az elem letöltésekor --------------------------
;; ----------------------------------------------------------------------------

; Az item-editor engine body komponensének {:query [...]} tulajdonságaként
; átadott Pathom lekérés vektor az elem letöltődésekor küldött lekéréssel
; összefűzve elküldésre kerül.
(defn my-query
  []
  [item-editor/body :my-editor {:form-element [:div "My form"]
                                :query        [:my-query]}])
