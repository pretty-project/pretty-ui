
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.keypress-handler.sample
    (:require [re-frame.api          :as r :refer [r]]
              [x.app-environment.api :as x.environment]))



;; -- Billentyű-esemény regisztrálása -----------------------------------------
;; ----------------------------------------------------------------------------

; Az [:environment/reg-keypress-event! ...] esemény meghívásával tudsz billentyű-lenyomásra
; vagy -felengedésre reagáló eseményeket regisztrálni.
(r/reg-event-fx :reg-my-keypress-event!
  [:environment/reg-keypress-event! :my-event {}])



;; -- Billentyű-esemény eltávolítása ------------------------------------------
;; ----------------------------------------------------------------------------

; Az [:environment/remove-keypress-event! ...] esemény meghívásával tudod eltávolítani az egyes
; regisztrált eseményeket, amennyiben az esemény regisztrálásakor használtál egyedi azonosítót.
(r/reg-event-fx :remove-my-keypress-event!
  [:environment/remove-keypress-event! :my-event])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Az (r environment/get-pressed-keys db) és az (r environment/key-pressed? db ...) feliratkozás
; függvények segítségével kiolvashatod a Re-Frame adatbázisból az aktuálisan lenyomott billenytyűket.
(defn my-subscription
  [db _]
  {:pressed-keys (r x.environment/get-pressed-keys db)
   :key-pressed? (r x.environment/key-pressed?     db 27)})
