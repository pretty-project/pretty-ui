
(ns x.app-core.sample
    (:require [app-fruits.window :as window]
              [x.app-core.api    :as a [:refer] r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn import-my-uri!
  [_]
  ; Külső forrásból a Re-Frame adatbázisba adatokat mellékhatás események használatával
  ; tudsz beolvasni.
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
