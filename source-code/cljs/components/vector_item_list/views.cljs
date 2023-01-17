
(ns components.vector-item-list.views
    (:require [components.illustration.views          :as illustration.views]
              [components.vector-item-list.attributes :as vector-item-list.attributes]
              [components.vector-item-list.prototypes :as vector-item-list.prototypes]
              [elements.api                           :as elements]
              [random.api                             :as random]
              [re-frame.api                           :as r]
              [x.components.api                       :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vector-item-list-placeholder
  ; @param (keyword) list-id
  ; @param (map) list-props
  ; {:placeholder (map)(opt)
  ;   {}}
  [_ {{:keys [illustration label]} :placeholder}]
  [:div {:class :c-vector-item-list--placeholder}
        (if illustration [illustration.views/component {:illustration illustration :height :s :width :s}])
        (if label        [elements/label {:content label :font-size :l :font-weight :bold}])])

(defn- vector-item-list-items
  ; @param (keyword) list-id
  ; @param (map) list-props
  ; {:item-element (symbol)
  ;  :value-path (vector)}
  [list-id {:keys [item-element value-path] :as list-props}]
  (letfn [(f [items item-dex _] (conj items [item-element item-dex]))]
         (let [items @(r/subscribe [:x.db/get-item value-path])]
              (if (empty? items)
                  [vector-item-list-placeholder list-id list-props]
                  (reduce-kv f [:<>] items)))))

(defn- vector-item-list
  ; @param (keyword) list-id
  ; @param (map) list-props
  [list-id list-props]
  [:div (vector-item-list.attributes/list-attributes list-id list-props)
        [:div (vector-item-list.attributes/list-body-attributes list-id list-props)
              [vector-item-list-items list-id list-props]]])

(defn component
  ; @param (keyword)(opt) list-id
  ; @param (map) list-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :indent (map)(opt)
  ;  :item-element (symbol)
  ;  :outdent (map)(opt)
  ;  :placeholder (map)(opt)
  ;   {:illustration (string)(opt)
  ;    :label (metamorphic-content)(opt)}
  ;  :style (map)(opt)
  ;  :value-path (vector)}
  ;
  ; @usage
  ; [vector-item-list {...}]
  ;
  ; @usage
  ; [vector-item-list :my-vector-item-list {...}]
  ;
  ; @usage
  ; (defn my-item-element [item-dex] ...)
  ; [vector-item-list :my-vector-item-list {:item-element #'my-item-element
  ;                                         :value-path   [:my-items]}]
  ([list-props]
   [component (random/generate-keyword) list-props])

  ([list-id list-props]
   (let [] ; list-props (vector-item-list.prototypes/list-props-prototype list-props)
        [vector-item-list list-id list-props])))
