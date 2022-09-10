
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.breadcrumbs.views
    (:require [x.app-components.api                  :as components]
              [x.app-core.api                        :as a]
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
  ;  {}
  [breadcrumbs-id breadcrumbs-props {:keys [label] :as crumb}]
  [:div.x-breadcrumbs--crumb (breadcrumbs.helpers/static-crumb-attributes breadcrumbs-id breadcrumbs-props crumb)
                             (components/content label)])

(defn- breadcrumbs-button-crumb
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; @param (map) crumb
  ;  {}
  [breadcrumbs-id breadcrumbs-props {:keys [label] :as crumb}]
  [:button.x-breadcrumbs--crumb (breadcrumbs.helpers/button-crumb-attributes breadcrumbs-id breadcrumbs-props crumb)
                                (components/content label)])

(defn- breadcrumbs-crumb
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; @param (map) crumb
  ;  {}
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
  [:div.x-breadcrumbs--body {}
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
  ;    [{:label (metamorphic-content)
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
   [element (a/id) breadcrumbs-props])

  ([breadcrumbs-id breadcrumbs-props]
   (let [breadcrumbs-props (breadcrumbs.prototypes/breadcrumbs-props-prototype breadcrumbs-props)]
        [breadcrumbs breadcrumbs-id breadcrumbs-props])))
