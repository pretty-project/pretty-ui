
(ns pretty-elements.menu-bar.prototypes
    (:require [dom.api                 :as dom]
              [metamorphic-content.api :as metamorphic-content]))

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
  ;  :href (string)(opt)
  ;  :icon (keyword)(opt)
  ;  :marker-color (keyword or string)(opt)
  ;  :on-click-f (function)(opt)}
  ;
  ; @return (map)
  ; {:badge-color (keyword or string)
  ;  :badge-content (string)
  ;  :badge-position (keyword)
  ;  :click-effect (keyword)
  ;  :font-size (keyword, px or string)
  ;  :font-weight (keyword or integer)
  ;  :icon-family (keyword)
  ;  :icon-size (keyword, px or string)
  ;  :line-height (keyword, px or string)
  ;  :on-mouse-up-f (function)}
  [{:keys [item-default]} {:keys [badge-content font-size href icon marker-color on-click-f] :as item-props}]
  (merge {:font-size   :s
          :font-weight :medium
          :icon-size   (or font-size :s)
          :line-height :text-block}
         (if badge-content {:badge-color :primary :badge-position :tr})
         (if marker-color  {:marker-position :tr})
         (if icon          {:icon-family :material-symbols-outlined})
         (if href          {:click-effect :opacity})
         (if on-click-f    {:click-effect :opacity})
         (-> item-default)
         (-> item-props)
         (if badge-content {:badge-content (metamorphic-content/compose badge-content)})
         (if on-click-f    {:on-mouse-up-f dom/blur-active-element!})))

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
