
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.image-preloader.views
    (:require [candy.api    :refer [param]]
              [random.api   :as random]
              [re-frame.api :as r]
              [reagent.api  :as reagent]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; @param (keyword)(opt) preloader-id
  ; @param (map) preloader-props
  ; {:uri (string)}
  ;
  ; @usage
  ; [component {...}]
  ;
  ; @usage
  ; [component :my-preloader {...}]
  ;
  ; @usage
  ; [component {:uri "/my-image.png"}]
  ([preloader-props]
   [component (random/generate-keyword) preloader-props])

  ([preloader-id {:keys [uri]}]
   (reagent/lifecycles {:component-will-mount                  #(r/dispatch [:x.core/start-synchron-signal! preloader-id])
                        :reagent-render (fn [] [:img {:on-load #(r/dispatch [:x.core/end-synchron-signal!   preloader-id])
                                                      :src      (param uri)
                                                      :style    {:display "none"}}])})))
