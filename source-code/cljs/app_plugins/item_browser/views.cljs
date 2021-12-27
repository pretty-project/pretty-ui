
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.3.2
; Compatibility: x4.5.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.views
    (:require [mid-fruits.candy     :refer [param return]]
              [x.app-db.api         :as db]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [x.app-layouts.api    :as layouts]
              [app-plugins.item-browser.engine :as engine]
              [app-plugins.item-lister.api     :as item-lister]))



;; -- Action components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- home-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:at-home? (boolean)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [at-home?]}]
  [elements/button ::home-button
                   {:disabled? at-home?
                    :on-click  (engine/go-home-event extension-id)
                    :preset    :home-icon-button}])

(defn- up-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:at-home? (boolean)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [at-home?]}]
  [elements/button ::up-button
                   {:disabled? at-home?
                    :on-click  (engine/go-up-event extension-id)
                    :preset    :up-icon-button}])



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header
  [extension-id item-namespace]
  [:div ;"header"
   ;(str (mid-fruits.string/use-replacements "%1 of %2 file downloaded" [3 3 3]))])
   ;(str (not (empty? [])))
   ;(str (some #(println %) [:a :b :c :b :d :d]))])
   ;(str (get [:a :b] 0))])
   ;(str (some keyword? ["a" :b]))])
   ;(str (mid-fruits.vector/nth-filtered [:a "a" :b "b" :c "c" :d "d" :e "e" :f "f"] keyword? 3))
   ;(str (mid-fruits.vector/compared-items-ordered? [0 1 3] [0 1 3 :a] <))])
;   (str (some #(keyword? %) ["a" "b" :c]))])
    (str (->> 2 (str "a" "b")))])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  [extension-id item-namespace body-props]
  [item-lister/body extension-id item-namespace body-props])



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- layout
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [extension-id item-namespace {:keys [description] :as view-props}]
  [layouts/layout-a extension-id {:body   {:content [body   extension-id item-namespace view-props]}
                                  :header {:content [header extension-id item-namespace]}
                                  :description description}])

(defn view
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) view-props
  ;  {:list-element (component)}
  ;
  ; @usage
  ;  [item-browser/view :my-extension :my-type {...}]
  ;
  ; @usage
  ;  (defn my-list-element [item-dex item] [:div ...])
  ;  [item-browser/view :my-extension :my-type {:element #'my-list-element}]
  ;
  ; @return (component)
  [extension-id item-namespace view-props]
  (let [subscribed-props (a/subscribe [:item-browser/get-view-props extension-id item-namespace])]
       (fn [] [layout extension-id item-namespace (merge view-props @subscribed-props)])))
