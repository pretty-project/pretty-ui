
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.sample
    (:require [plugins.item-editor.api :as item-editor]
              [re-frame.api            :as r]))



;; -- Szerver-oldali beállítás ------------------------------------------------
;; ----------------------------------------------------------------------------

; A plugin beállításához mindenképpen szükséges a szerver-oldali
; [:item-editor/init-editor! ...] eseményt használni!



;; -- A plugin használata alapbeállításokkal ----------------------------------
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



;; -- Az {:initial-item {...}} tulajdonság használata -------------------------
;; ----------------------------------------------------------------------------

; - Az item-editor plugin számára átadott form-element komponensben ne használj olyan input mezőt,
;   ami {:initial-value ...} tulajdonsággal rendelkezik, mert ...
;   Pl. Új elem létrehozásakor az input mezők {:initial-value ...} értékei megváltoztatják a dokumentumot,
;        és ha a felhasználó a dokumentum változtatása nélkül elhagyja a szerkesztőt, akkor az tévesen
;        úgy érzékelné, hogy a dokumentumot a felhasználó változtatta meg és az elhagyás után felajánlaná
;        a "Nem mentett változtatások visszaállítása" lehetőségét!
;
; - ...



;; -- Az [:item-editor/edit-item! "..."] esemény hanszálata -------------------
;; ----------------------------------------------------------------------------

; ...
(r/reg-event-fx
  :edit-my-item!
  [:item-editor/edit-item! :my-editor "my-item"])



;; -- Az {:item-id "..."} paraméter használata --------------------------------
;; ----------------------------------------------------------------------------

; A body komponens {:item-id "..."} paraméterének értékét használja az aktuálisan
; szerkesztett elem azonosítójaként, ...
; ... amikor az aktuális útvonalból nem származtatható az :item-id útvonal-paraméter.
; ... a plugin NEM útvonal-vezérelt.



;; -- Pathom lekérés használata az elem letöltésekor --------------------------
;; ----------------------------------------------------------------------------

; Az item-editor plugin body komponensének {:query [...]} tulajdonságaként
; átadott Pathom lekérés vektor az elem letöltődésekor küldött lekéréssel
; összefűzve elküldésre kerül.
(defn my-query
  []
  [item-editor/body :my-editor {:form-element [:div "My form"]
                                :query        [:my-query]}])
