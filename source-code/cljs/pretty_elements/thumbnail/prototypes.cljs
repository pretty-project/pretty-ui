
(ns pretty-elements.thumbnail.prototypes
    (:require [dom.api              :as dom]
              [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn thumbnail-props-prototype
  ; @ignore
  ;
  ; @param (map) thumbnail-props
  ; {:border-color (keyword or string)(opt)
  ;  :href (string)(opt)
  ;  :on-click-f (function)(opt)}
  ;
  ; @return (map)
  ; {:background-size (keyword)
  ;  :border-color (keyword or string)
  ;  :border-position (keyword)
  ;  :border-width (keyword, px or string)
  ;  :click-effect (keyword)
  ;  :height (keyword, px or string)
  ;  :icon (keyword)
  ;  :icon-family (keyword)
  ;  :on-mouse-up-f (function)
  ;  :width (keyword, px or string)}
  [{:keys [border-color href on-click-f] :as thumbnail-props}]
  (merge {:background-size :contain
          :height          :s
          :icon            :image
          :icon-family     :material-symbols-outlined
          :width           :s}
         (if href         {:click-effect :opacity})
         (if on-click-f   {:click-effect :opacity})
         (if border-color {:border-position :all
                           :border-width    :xxs})
         (-> thumbnail-props)
         (if href         {:on-mouse-up-f dom/blur-active-element!})
         (if on-click-f   {:on-mouse-up-f dom/blur-active-element!})))
