
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.build-handler.side-effects
    (:require [candy.api                             :refer [return]]
              [format.api                            :as format]
              [io.api                                :as io]
              [mid.x.core.build-handler.side-effects :as build-handler.side-effects]
              [re-frame.api                          :as r]
              [x.core.build-handler.config           :as build-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.x.core.build-handler.side-effects
(def build-version build-handler.side-effects/build-version)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-build-version!
  ; @param (keyword or string) build-version
  ;  :auto
  ;
  ; @usage
  ;  (update-build-version! :auto)
  ;
  ; @usage
  ;  (update-build-version! "0.4.2.0")
  ;
  ; @return (string)
  [build-version]
  ; Az applikáció build-version számának értékét beolvassa az azt tároló fájlból,
  ; majd növeli eggyel és a növelt értéket eltárolja a fájlban.
  (letfn [(get-auto-version [] (if-let [{:keys [build-version]} (io/read-edn-file build-handler.config/BUILD-VERSION-FILEPATH)]
                                       (format/inc-version build-version)
                                       (return build-handler.config/INITIAL-BUILD-VERSION)))]
         (cond (string? build-version) (io/write-edn-file! build-handler.config/BUILD-VERSION-FILEPATH {:build-version build-version}      {:create? true})
               (= :auto build-version) (io/write-edn-file! build-handler.config/BUILD-VERSION-FILEPATH {:build-version (get-auto-version)} {:create? true}))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn import-build-version!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  ; Development környezetben nincs szükség az applikáció build-version számának
  ; használatára ezért nem szükséges az (update-build-version!) függvényt meghívni,
  ; ami az aktuális build-version értékét eltárolná egy fájlban.
  ;
  ; Ezért development környezetben nem minden esetben biztosított a build-version
  ; értékét tároló fájl létezése.
  ;
  ; Ha a build-version értékét tároló fájl nem elérhető, akkor a build-version értéke
  ; egy alapértelmezett értékkel kerül behelyettesítésre (pl. "0.0.1").
  (if (io/file-exists? build-handler.config/BUILD-VERSION-FILEPATH)
      (let [{:keys [build-version]} (io/read-edn-file build-handler.config/BUILD-VERSION-FILEPATH)]
           (r/dispatch [:x.core/store-build-version! build-version]))
      (r/dispatch [:x.core/store-build-version! build-handler.config/INITIAL-BUILD-VERSION])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :x.core/import-build-version! import-build-version!)
