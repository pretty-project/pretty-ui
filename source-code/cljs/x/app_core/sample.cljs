
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.sample
    (:require [app-fruits.window :as window]
              [x.app-core.api    :as a [:refer] r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; A Re-Frame adatbázisba külső forrásból adatokat mellékhatás események használatával tudsz beolvasni.
(defn import-my-uri!
  [_]
  (let [uri (window/get-uri)]
       (a/dispatch [:db/set-item! [:uri] uri])))

; Ha nevesített függvényt regisztrálsz mellékhatás eseményként, akkor a függvény a Re-frame
; rendszeren kívülről is meghívható lesz.
(a/reg-fx :import-my-uri! import-my-uri!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; A szerver-oldali :my-transfer azonosítóval regisztrált függvény visszatérési értékének
; kiolvasása a kliens-oldali Re-Frame adatbázisból az a/get-transfer-data függvénnyel.
(defn get-my-transfer-data
  [db _]
  (r a/get-transfer-data db :my-transfer))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-switch-enabled?
  [db _]
  (get-in db [:my-switch]))

(defn toggle-my-switch!
  [db [_ data]]
  (update-in db [:my-switch] not))

(a/reg-event-fx
  :speed-up-events!
  (fn [{:keys [db]} _]
      ; A.
      (if (r my-switch-enabled? db)
          {:db (r toggle-my-switch! db)})
      ; B.
      (let [db (r toggle-my-switch! db)]
           (if (r my-switch-enabled? db)
               {:db db}))))
