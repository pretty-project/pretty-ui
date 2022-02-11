
; WARNING! EXPIRED! DO NOT USE!



;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.12.21
; Description: Az applikáció indításának várakoztatása az egyes képek sikeres letöltéséig
; Version: v0.2.6
; Compatibility:



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.image-preloader
    (:require [app-fruits.reagent :as reagent]
              [mid-fruits.candy   :refer [param]]
              [x.app-core.api     :as a]))



;; -- Usage -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [tools/image-preloader {:uri "/my-image.png"}]



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; @param (keyword)(opt) preloader-uri
  ; @param (map) preloader-props
  ;  {:uri (string)}
  ;
  ; @usage
  ;  [tools/image-preloader {...}]
  ;
  ; @usage
  ;  [tools/image-preloader :my-preloader {...}]
  ;
  ; @usage
  ;  [tools/image-preloader {:uri "/my-image.png"}]
  ;
  ; @return (component)
  ([preloader-props]
   [component (a/id) preloader-props])

  ([preloader-id {:keys [uri]}]
   (reagent/lifecycles {:component-will-mount #(a/dispatch [:core/synchronize-loading!      preloader-id])
                        :reagent-render [:img {:on-load #(a/dispatch [:core/synchron-signal preloader-id])
                                               :src      (param uri)
                                               :style    {:display "none"}}]})))
