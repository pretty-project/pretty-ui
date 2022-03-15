
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.image-preloader.views
    (:require [app-fruits.reagent :as reagent]
              [mid-fruits.candy   :refer [param]]
              [x.app-core.api     :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; @param (keyword)(opt) preloader-id
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
  ([preloader-props]
   [component (a/id) preloader-props])

  ([preloader-id {:keys [uri]}]
   (reagent/lifecycles {:component-will-mount           #(a/dispatch [:core/start-synchron-signal! preloader-id])
                        :reagent-render [:img {:on-load #(a/dispatch [:core/end-synchron-signal!   preloader-id])
                                               :src      (param uri)
                                               :style    {:display "none"}}]})))
