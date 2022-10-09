
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-environment.css-handler.sample
    (:require [re-frame.api             :as r :refer [r]]
              [x.server-core.api        :as core]
              [x.server-environment.api :as environment]))



;; -- CSS fájl hozzáadása a szerver indulásakor Re-Frame eseménnyel -----------
;; ----------------------------------------------------------------------------

; - A :core-js opcionális tulajdonság  használatával beállítható, hogy melyik
;   .js fájl (pl. app.js) használatakor töltődjön be az adott .css fájl
;
; - Ha egy .css fájlt egy-egy útvonal használatához szeretnéd kapcsolni,
;   akkor használd a kliens-oldali [:environment/add-css! ...] eseményt!
(core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:environment/add-css! {:core-js "sample.js"
                                           :uri     "/css/sample.css"}]})



;; -- CSS fájl hozzáadása Re-Frame DB függvénnyel -----------------------------
;; ----------------------------------------------------------------------------

(defn add-my-css!
  [db _]
  (r environment/add-css! db {:core-js "sample.js"
                              :uri     "/css/sample.css"}))

(r/reg-event-db :add-my-css! add-my-css!)
