
(ns pretty-elements.menu-bar.prototypes
    (:require [metamorphic-content.api :as metamorphic-content]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-props-prototype
  ; @ignore
  ;
  ; @param (map) bar-props
  ; {:item-default (map)}
  ; @param (map) item-props
  ; {:badge-content (metamorphic-content)(opt)
  ;  :icon (keyword)(opt)}
  ;  :marker-color (keyword)(opt)}
  ;
  ; @return (map)
  ; {:badge-color (keyword)
  ;  :badge-content (string)
  ;  :badge-position (keyword)
  ;  :icon-family (keyword)}
  [{:keys [item-default]} {:keys [badge-content icon marker-color] :as item-props}]
  (merge {:font-size   :s
          :font-weight :medium
          :icon-size   :s
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
  ; {:orientation (keyword)(opt)}
  ;
  ; @return (map)
  ; {:horizontal-align (keyword)
  ;  :orientation (keyword)}
  [{:keys [orientation] :as bar-props}]
  (merge {:orientation :horizontal}
         (if-not (= orientation :vertical)
                 {:horizontal-align :left})
         (-> bar-props)))
