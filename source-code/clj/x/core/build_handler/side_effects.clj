
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.build-handler.side-effects
    (:require [format.api                            :as format]
              [io.api                                :as io]
              [mid.x.core.build-handler.side-effects :as build-handler.side-effects]
              [re-frame.api                          :as r]
              [x.core.build-handler.config           :as build-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.x.core.build-handler.side-effects
(def app-build build-handler.side-effects/app-build)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ->app-built
  ; @usage
  ;  (->app-built)
  ;
  ; @return (string)
  []
  ; Az app-build (az applikáció build-version számának) értékét beolvassa az azt
  ; tároló fájlból, majd növeli eggyel és a növelt értéket eltárolja a fájlban.
  (letfn [(f [{:keys [app-build]}] (if app-build {:app-build (format/inc-version app-build)}
                                                 {:app-build build-handler.config/INITIAL-APP-BUILD}))]
         (io/swap-edn-file! build-handler.config/APP-BUILD-FILEPATH f)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn import-app-build!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  ; Development környezetben nincs szükség az app-build (az applikáció build-version számának)
  ; használatára ezért nem szükséges az (a/->app-built) függvényt meghívni, ami az aktuális
  ; app-build értékét eltárolná egy fájlban.
  ;
  ; Ezért development környezetben nem minden esetben biztosított az app-build értékét tároló
  ; fájl létezése.
  ;
  ; Ha az app-build értékét tároló fájl nem elérhető, akkor az app-build értéke egy alapértelmezett
  ; értékkel kerül behelyettesítésre (pl. "0.0.1").
  (if (io/file-exists? build-handler.config/APP-BUILD-FILEPATH)
      (let [{:keys [app-build]} (io/read-edn-file build-handler.config/APP-BUILD-FILEPATH)]
           (r/dispatch [:x.core/store-app-build! app-build]))
      (r/dispatch [:x.core/store-app-build! build-handler.config/INITIAL-APP-BUILD])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :x.core/import-app-build! import-app-build!)