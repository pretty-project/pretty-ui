
(ns elements.breadcrumbs.views
    (:require [elements.breadcrumbs.helpers    :as breadcrumbs.helpers]
              [elements.breadcrumbs.prototypes :as breadcrumbs.prototypes]
              [random.api                      :as random]
              [x.components.api                :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- breadcrumbs-separator
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  [_ _]
  [:div.e-breadcrumbs--separator])

(defn- breadcrumbs-static-crumb
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; @param (map) crumb
  ; {:label (metamorphic-content)(opt)
  ;  :placeholder (metamorphic-content)(opt)}
  [breadcrumbs-id breadcrumbs-props {:keys [label placeholder] :as crumb}]
  [:div.e-breadcrumbs--crumb (breadcrumbs.helpers/static-crumb-attributes breadcrumbs-id breadcrumbs-props crumb)
                             (if (-> label       x.components/content empty?)
                                 (-> placeholder x.components/content)
                                 (-> label       x.components/content))])

(defn- breadcrumbs-button-crumb
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; @param (map) crumb
  ; {:label (metamorphic-content)(opt)
  ;  :placeholder (metamorphic-content)(opt)}
  [breadcrumbs-id breadcrumbs-props {:keys [label placeholder] :as crumb}]
  [:button.e-breadcrumbs--crumb (breadcrumbs.helpers/button-crumb-attributes breadcrumbs-id breadcrumbs-props crumb)
                                (if (-> label       x.components/content empty?)
                                    (-> placeholder x.components/content)
                                    (-> label       x.components/content))])

(defn- breadcrumbs-crumb
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; @param (map) crumb
  ; {:route (string)(opt)
  [breadcrumbs-id breadcrumbs-props {:keys [route] :as crumb}]
  (if route [breadcrumbs-button-crumb breadcrumbs-id breadcrumbs-props crumb]
            [breadcrumbs-static-crumb breadcrumbs-id breadcrumbs-props crumb]))

(defn- breadcrumbs-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; {:crumbs (maps in vector)}
  [breadcrumbs-id {:keys [crumbs] :as breadcrumbs-props}]
  [:div.e-breadcrumbs--body (breadcrumbs.helpers/breadcrumbs-body-attributes breadcrumbs-id breadcrumbs-props)
                            (letfn [(f [crumb-list dex crumb]
                                       (conj crumb-list (if (not= dex 0)
                                                            [:<> [breadcrumbs-separator breadcrumbs-id breadcrumbs-props crumb]
                                                                 [breadcrumbs-crumb     breadcrumbs-id breadcrumbs-props crumb]]
                                                            [:<> [breadcrumbs-crumb     breadcrumbs-id breadcrumbs-props crumb]])))]
                                   (reduce-kv f [:<>] crumbs))])

(defn- breadcrumbs
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  [breadcrumbs-id breadcrumbs-props]
  [:div.e-breadcrumbs (breadcrumbs.helpers/breadcrumbs-attributes breadcrumbs-id breadcrumbs-props)
                      [breadcrumbs-body                           breadcrumbs-id breadcrumbs-props]])

(defn element
  ; @param (keyword)(opt) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :crumbs (maps in vector)
  ;   [{:label (metamorphic-content)(opt)
  ;     :placeholder (metamorphic-content)(opt)
  ;     :route (string)(opt)}]
  ;  :disabled? (boolean)(opt)
  ;   Default: false
  ;  :font-size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :inherit
  ;   Default: :s
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :left (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :right (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :top (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
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
   (let [];breadcrumbs-props (breadcrumbs.prototypes/breadcrumbs-props-prototype breadcrumbs-props)
        [breadcrumbs breadcrumbs-id breadcrumbs-props])))
