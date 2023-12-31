
(ns pretty-elements.menu-bar.prototypes
    (:require [metamorphic-content.api :as metamorphic-content]
              [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-props-prototype
  ; @ignore
  ;
  ; @param (map) bar-props
  ; {:item-default (map)}
  ; @param (map) item-props
  ; {:badge-content (metamorphic-content)(opt)
  ;  :font-size (keyword, px or string)(opt)
  ;  :icon (keyword)(opt)
  ;  :marker-color (keyword or string)(opt)}
  ;
  ; @return (map)
  ; {:badge-color (keyword or string)
  ;  :badge-content (string)
  ;  :badge-position (keyword)
  ;  :font-size (keyword, px or string)
  ;  :font-weight (keyword or integer)
  ;  :icon-family (keyword)
  ;  :icon-size (keyword, px or string)
  ;  :line-height (keyword, px or string)}
  [{:keys [item-default]} {:keys [badge-content font-size icon marker-color] :as item-props}]
  (merge {:font-size   :s
          :font-weight :medium
          :icon-size   (or font-size :s)
          :line-height :text-block}
         (if badge-content {:badge-color :primary :badge-position :tr})
         (if marker-color  {:marker-position :tr})
         (if icon          {:icon-family :material-symbols-outlined})
         (-> item-default)
         (-> item-props)
         (if badge-content {:badge-content (metamorphic-content/compose badge-content)})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bar-props-prototype
  ; @ignore
  ;
  ; @param (map) bar-props
  ;
  ; @return (map)
  ; {:orientation (keyword)}
  [bar-props]
  (merge {:orientation :horizontal}
         (-> bar-props)))
