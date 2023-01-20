
(ns elements.menu-bar.prototypes
    (:require [candy.api        :refer [param]]
              [x.components.api :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
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
  (merge (if badge-content {:badge-color :primary :badge-position :tr})
         (if marker-color  {:marker-position :tr})
         (if icon          {:icon-family :material-symbols-outlined})
         {:font-size   :s
          :font-weight :medium
          :icon-size   :s
          :line-height :text-block}
         (param item-default)
         (param item-props)
         (if badge-content {:badge-content (x.components/content badge-content)})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bar-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
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
         (param bar-props)))
