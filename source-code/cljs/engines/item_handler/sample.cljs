
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.sample
    (:require [engines.item-handler.api :as item-handler]
              [re-frame.api             :as r]))



;; -- Szerver-oldali beállítás ------------------------------------------------
;; ----------------------------------------------------------------------------

; Az engine beállításához mindenképpen szükséges a szerver-oldali
; [:item-handler/init-handler! ...] eseményt használni!



;; -- Az engine használata alapbeállításokkal ----------------------------------
;; ----------------------------------------------------------------------------

(defn my-handler
  []
  [item-handler/body :my-handler {:item-element [:div "My item"]}])



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



;; -- Az [:item-handler/handle-item! "..."] esemény hanszálata ------------------
;; ----------------------------------------------------------------------------

; ...
(r/reg-event-fx :handle-my-item!
  [:item-handler/handle-item! :my-handler "my-item"])



;; -- Az {:item-id "..."} paraméter használata --------------------------------
;; ----------------------------------------------------------------------------

; A body komponens {:item-id "..."} paraméterének értékét használja az aktuálisan
; kezelt elem azonosítójaként, ...
; ... amikor az aktuális útvonalból nem származtatható az :item-id útvonal-paraméter.
; ... az engine NEM útvonal-vezérelt.



;; -- Pathom lekérés használata az elem letöltésekor --------------------------
;; ----------------------------------------------------------------------------

; Az item-handler engine body komponensének {:query [...]} tulajdonságaként
; átadott Pathom lekérés vektor az elem letöltődésekor küldött lekéréssel
; összefűzve elküldésre kerül.
(defn my-query
  []
  [item-handler/body :my-handler {:item-element [:div "My item"]
                                  :query        [:my-query]}])
