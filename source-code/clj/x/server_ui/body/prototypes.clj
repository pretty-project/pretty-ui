

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.body.prototypes
    (:require [mid-fruits.vector            :as vector]
              [x.server-core.api            :as a]
              [x.server-router.api          :as router]
              [x.server-ui.graphics.views   :as graphics.views]
              [x.server-ui.shield.views     :refer [view] :rename {view app-shield}]
              [x.server-user.api            :as user]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ; @param (map) body-props
  ;  {:plugin-js-paths (maps in vector)(opt)}
  ;
  ; @return (map)
  ;  {:app-build (string)
  ;   :core-js (string)
  ;   :selected-theme (string)
  ;   :shield (hiccup)}
  [request body-props]
  (let [app-config @(a/subscribe [:core/get-app-config])]
       (merge app-config body-props
              {:app-build      (a/app-build)
               :core-js        (router/request->core-js          request)
               :selected-theme (user/request->user-settings-item request :selected-theme)
               :shield         (app-shield (graphics.views/loading-animation))
               ; Hozzáadja a {:plugin-js-paths [...]} paraméterként átadott útvonalakat
               ; az x.app-config.edn fájlban beállított útvonalakhoz
               :plugin-js-paths (vector/concat-items (:plugin-js-paths app-config)
                                                     (:plugin-js-paths body-props))})))
