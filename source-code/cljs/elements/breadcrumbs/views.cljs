
(ns elements.breadcrumbs.views
    (:require [elements.breadcrumbs.attributes :as breadcrumbs.attributes]
              [elements.breadcrumbs.prototypes :as breadcrumbs.prototypes]
              [random.api                      :as random]
              [x.components.api                :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- breadcrumbs-crumb-label
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; @param (map) crumb
  ; {:label (metamorphic-content)(opt)
  ;  :placeholder (metamorphic-content)(opt)}
  [_ _ {:keys [label placeholder]}]
  (if (-> label       x.components/content empty?)
      (-> placeholder x.components/content)
      (-> label       x.components/content)))

(defn- breadcrumbs-crumb
  ; @ignore
  ;
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; @param (map) crumb
  ; {:route (string)(opt)
  [breadcrumbs-id breadcrumbs-props {:keys [route] :as crumb}]
  (if route [:button (breadcrumbs.attributes/button-crumb-attributes breadcrumbs-id breadcrumbs-props crumb)
                     (breadcrumbs-crumb-label                        breadcrumbs-id breadcrumbs-props crumb)]
            [:div    (breadcrumbs.attributes/static-crumb-attributes breadcrumbs-id breadcrumbs-props crumb)
                     (breadcrumbs-crumb-label                        breadcrumbs-id breadcrumbs-props crumb)]))

(defn- breadcrumbs-crumb-list
  ; @ignore
  ;
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; {:crumbs (maps in vector)}
  [breadcrumbs-id {:keys [crumbs] :as breadcrumbs-props}]
  (letfn [(f [crumb-list dex crumb] (conj crumb-list (if (not= dex 0) [:div {:class :e-breadcrumbs--separator}])
                                                     [breadcrumbs-crumb breadcrumbs-id breadcrumbs-props crumb]))]
         (reduce-kv f [:<>] crumbs)))

(defn- breadcrumbs
  ; @ignore
  ;
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  [breadcrumbs-id breadcrumbs-props]
  [:div (breadcrumbs.attributes/breadcrumbs-attributes breadcrumbs-id breadcrumbs-props)
        [:div (breadcrumbs.attributes/breadcrumbs-body-attributes breadcrumbs-id breadcrumbs-props)
              [breadcrumbs-crumb-list                             breadcrumbs-id breadcrumbs-props]]])

(defn element
  ; @param (keyword)(opt) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :crumbs (maps in vector)
  ;   [{:label (metamorphic-content)(opt)
  ;     :placeholder (metamorphic-content)(opt)
  ;     :route (string)(opt)}]
  ;  :disabled? (boolean)(opt)
  ;  :font-size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :inherit
  ;   Default: :s
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :outdent (map)(opt)
  ;   Same as the :indent property
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [breadcrumbs {...}]
  ;
  ; @usage
  ; [breadcrumbs :my-breadcrumbs {...}]
  ([breadcrumbs-props]
   [element (random/generate-keyword) breadcrumbs-props])

  ([breadcrumbs-id breadcrumbs-props]
   (let [] ; breadcrumbs-props (breadcrumbs.prototypes/breadcrumbs-props-prototype breadcrumbs-props)
        [breadcrumbs breadcrumbs-id breadcrumbs-props])))
