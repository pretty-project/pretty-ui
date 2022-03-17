
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.keypress-handler.sample
    (:require [x.app-core.api        :as a :refer [r]]
              [x.app-environment.api :as environment]))



;; -- Billentyű-esemény regisztrálása -----------------------------------------
;; ----------------------------------------------------------------------------

; Az [:environment/reg-keypress-event! ...] esemény meghívásával tudsz billentyű-lenyomásra
; vagy -felengedésre reagáló eseményeket regisztrálni.
(a/reg-event-fx
  :reg-my-keypress-event!
  [:environment/reg-keypress-event! :my-event {}])



;; -- Billentyű-esemény eltávolítása ------------------------------------------
;; ----------------------------------------------------------------------------

; Az [:environment/remove-keypress-event! ...] esemény meghívásával tudod eltávolítani az egyes
; regisztrált eseményeket, amennyiben az esemény regisztrálásakor használtál egyedi azonosítót.
(a/reg-event-fx
  :remove-my-keypress-event!
  [:environment/remove-keypress-event! :my-event])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Az (r environment/get-pressed-keys db) és az (r environment/key-pressed? db ...) feliratkozás
; függvények segítségével kiolvashatod a Re-Frame adatbázisból az aktuálisan lenyomott billenytyűket.
(defn my-subscription
  [db _]
  {:pressed-keys (r environment/get-pressed-keys db)
   :key-pressed? (r environment/key-pressed?     db 27)})