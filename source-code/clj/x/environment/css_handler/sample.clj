
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.environment.css-handler.sample
    (:require [re-frame.api      :as r :refer [r]]
              [x.core.api        :as x.core]
              [x.environment.api :as x.environment]))



;; -- CSS fájl hozzáadása a szerver indulásakor Re-Frame eseménnyel -----------
;; ----------------------------------------------------------------------------

; A :js-build opcionális tulajdonság  használatával beállítható, hogy melyik
; JS build (pl. :site) használatakor töltődjön be az adott .css fájl
;
; Ha egy .css fájlt egy-egy útvonal használatához szeretnéd kapcsolni,
; akkor használd a kliens-oldali [:x.environment/add-css! ...] eseményt!
(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:x.environment/add-css! {:js-build :sample
                                             :uri      "/css/sample.css"}]})



;; -- CSS fájl hozzáadása Re-Frame DB függvénnyel -----------------------------
;; ----------------------------------------------------------------------------

(defn add-my-css!
  [db _]
  (r x.environment/add-css! db {:js-build :sample
                                :uri      "/css/sample.css"}))

(r/reg-event-db :add-my-css! add-my-css!)
