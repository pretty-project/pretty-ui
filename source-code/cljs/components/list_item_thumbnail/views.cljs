
(ns components.list-item-thumbnail.views
    (:require [pretty-elements.api :as pretty-elements]
              [random.api   :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-thumbnail-body
  ; @param (keyword)(opt) thumbnail-id
  ; @param (map) thumbnail-props
  ; {:icon (keyword)(opt)
  ;  :icon-family (keyword)(opt)
  ;  :thumbnail (string)(opt)}
  [_ {:keys [icon icon-family thumbnail]}]
  (cond icon-family      [:div.c-list-item-thumbnail--icon [pretty-elements/icon {:icon icon :icon-family icon-family}]]
        icon             [:div.c-list-item-thumbnail--icon [pretty-elements/icon {:icon icon}]]
        thumbnail        [pretty-elements/thumbnail {:border-radius {:all :s} :height :s :indent {:horizontal :xxs} :uri thumbnail :width :l}]
        :empty-thumbnail [pretty-elements/thumbnail {:border-radius {:all :s} :height :s :indent {:horizontal :xxs}                :width :l}]))

(defn- list-item-thumbnail
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  [thumbnail-id thumbnail-props]
  [:div.c-list-item-thumbnail [list-item-thumbnail-body thumbnail-id thumbnail-props]])

(defn component
  ; @param (keyword)(opt) thumbnail-id
  ; @param (map) thumbnail-props
  ; {:icon (keyword)(opt)
  ;  :icon-family (keyword)(opt)
  ;  :thumbnail (string)(opt)}
  ;
  ; @usage
  ; [list-item-thumbnail {...}]
  ;
  ; @usage
  ; [list-item-thumbnail :my-thumbnail {...}]
  ;
  ; @usage
  ; [list-item-thumbnail {:thumbnail "/my-thumbnail.png"}]
  ;
  ; @usage
  ; [list-item-thumbnail {:icon :people}]
  ([thumbnail-props]
   [component (random/generate-keyword) thumbnail-props])

  ([thumbnail-id thumbnail-props]
   (fn [_ thumbnail-props] ; XXX#0106 (README.md#parametering)
       (let [] ; thumbnail-props (list-item-thumbnail.prototypes/thumbnail-props-prototype thumbnail-props)
            [list-item-thumbnail thumbnail-id thumbnail-props]))))
