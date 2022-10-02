
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.breadcrumbs.views
    (:require [mid-fruits.random                     :as random]
              [x.app-components.api                  :as components]
              [x.app-elements.breadcrumbs.helpers    :as breadcrumbs.helpers]
              [x.app-elements.breadcrumbs.prototypes :as breadcrumbs.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- breadcrumbs-static-crumb
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; @param (map) crumb
  ;  {:label (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)}
  [breadcrumbs-id breadcrumbs-props {:keys [label placeholder] :as crumb}]
  [:div.x-breadcrumbs--crumb (breadcrumbs.helpers/static-crumb-attributes breadcrumbs-id breadcrumbs-props crumb)
                             ; BUG#3400
                             (if (-> label       components/content str empty?)
                                 (-> placeholder components/content)
                                 (-> label       components/content))])

(defn- breadcrumbs-button-crumb
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; @param (map) crumb
  ;  {:label (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)}
  [breadcrumbs-id breadcrumbs-props {:keys [label placeholder] :as crumb}]
  [:button.x-breadcrumbs--crumb (breadcrumbs.helpers/button-crumb-attributes breadcrumbs-id breadcrumbs-props crumb)
                                ; BUG#3400
                                (if (-> label       components/content str empty?)
                                    (-> placeholder components/content)
                                    (-> label       components/content))])

(defn- breadcrumbs-crumb
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; @param (map) crumb
  ;  {:route (string)(opt)
  [breadcrumbs-id breadcrumbs-props {:keys [route] :as crumb}]
  (if route [breadcrumbs-button-crumb breadcrumbs-id breadcrumbs-props crumb]
            [breadcrumbs-static-crumb breadcrumbs-id breadcrumbs-props crumb]))

(defn- breadcrumbs-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ;  {:crumbs (maps in vector)}
  [breadcrumbs-id {:keys [crumbs] :as breadcrumbs-props}]
  [:div.x-breadcrumbs--body (breadcrumbs.helpers/breadcrumbs-body-attributes breadcrumbs-id breadcrumbs-props)
                            (letfn [(f [crumb-list crumb]
                                       (conj crumb-list [breadcrumbs-crumb breadcrumbs-id breadcrumbs-props crumb]))]
                                   (reduce f [:<>] crumbs))])

(defn- breadcrumbs
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  [breadcrumbs-id breadcrumbs-props]
  [:div.x-breadcrumbs (breadcrumbs.helpers/breadcrumbs-attributes breadcrumbs-id breadcrumbs-props)
                      [breadcrumbs-body                           breadcrumbs-id breadcrumbs-props]])

(defn element
  ; @param (keyword)(opt) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :crumbs (maps in vector)
  ;    [{:label (metamorphic-content)(opt)
  ;      :placeholder (metamorphic-content)(opt)
  ;      :route (string)(opt)}]
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :font-size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [elements/breadcrumbs {...}]
  ;
  ; @usage
  ;  [elements/breadcrumbs :my-breadcrumbs {...}]
  ([breadcrumbs-props]
   [element (random/generate-keyword) breadcrumbs-props])

  ([breadcrumbs-id breadcrumbs-props]
   (let [breadcrumbs-props (breadcrumbs.prototypes/breadcrumbs-props-prototype breadcrumbs-props)]
        [breadcrumbs breadcrumbs-id breadcrumbs-props])))