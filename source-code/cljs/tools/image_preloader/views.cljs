
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.image-preloader.views
    (:require [mid-fruits.candy  :refer [param]]
              [mid-fruits.random :as random]
              [reagent.api       :as reagent]
              [x.app-core.api    :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; @param (keyword)(opt) preloader-id
  ; @param (map) preloader-props
  ;  {:uri (string)}
  ;
  ; @usage
  ;  [image-preloader/component {...}]
  ;
  ; @usage
  ;  [image-preloader/component :my-preloader {...}]
  ;
  ; @usage
  ;  [image-preloader/component {:uri "/my-image.png"}]
  ([preloader-props]
   [component (random/generate-keyword) preloader-props])

  ([preloader-id {:keys [uri]}]
   (reagent/lifecycles {:component-will-mount           #(a/dispatch [:core/start-synchron-signal! preloader-id])
                        :reagent-render [:img {:on-load #(a/dispatch [:core/end-synchron-signal!   preloader-id])
                                               :src      (param uri)
                                               :style    {:display "none"}}]})))
