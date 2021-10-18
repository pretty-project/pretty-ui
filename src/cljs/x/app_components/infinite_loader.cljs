
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.18
; Description:
; Version: v0.2.6
; Compatibility: x4.4.1



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-components.infinite-loader
    (:require [app-fruits.dom     :as dom]
              [app-fruits.reagent :refer [ratom lifecycles]]
              [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.random  :as random]
              [mid-fruits.vector  :as vector]
              [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- loader-id->observer-id
  ; @param (keyword) loader-id
  ;
  ; @example
  ;  (loader-id->observer-id :my-loader)
  ;  => :my-loader--observer
  ;
  ; @return (keyword)
  [loader-id]
  (keyword/append loader-id :observer "--"))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- infinite-loader-hidden?
  ; @param (keyword) loader-id
  ;
  ; @return (boolean)
  [db [_ loader-id]]
  (let [visible? (get-in db (db/path ::infinite-loaders loader-id :visible?))]
      ;(= visible? nil) = (= visible? true)
       (= visible? false)))

(a/reg-sub :x.app-components/infinite-loader-hidden? infinite-loader-hidden?)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn hide-infinite-loader!
  ; @param (keyword) loader-id
  ;
  ; @usage
  ;  (r components/hide-infinite-loader! db :my-loader)
  ;
  ; @return (map)
  [db [_ loader-id]]
  (assoc-in db (db/path ::infinite-loaders loader-id :visible?)
               (param false)))

; @usage
;  [:x.app-components/hide-infinite-loader! :my-loader]
(a/reg-event-db :x.app-components/hide-infinite-loader! hide-infinite-loader!)

(defn show-infinite-loader!
  ; @param (keyword) loader-id
  ;
  ; @usage
  ;  (r components/show-infinite-loader! db :my-loader)
  ;
  ; @return (map)
  [db [_ loader-id]]
  (assoc-in db (db/path ::infinite-loaders loader-id :visible?)
               (param true)))

; @usage
;  [:x.app-components/show-infinite-loader! :my-loader]
(a/reg-event-db :x.app-components/show-infinite-loader! show-infinite-loader!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-components/reload-infinite-loader!
  ; @param (keyword) loader-id
  (fn [{:keys [db]} [_ loader-id]]
      {:db (r hide-infinite-loader! db loader-id)
       :dispatch-later [{:ms 50 :dispatch [:x.app-components/show-infinite-loader! loader-id]}]}))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- observer
  ; @param (keyword) loader-id
  ; @param (boolean) hidden?
  ;
  ; @return (component)
  [loader-id hidden?]
  (let [observer-id (loader-id->observer-id loader-id)]
       [:div {:id    (keyword/to-dom-value observer-id)
              :style (if (boolean hidden?)
                         {:position :fixed :bottom "-100px"})}]))

(defn- infinite-loader
  ; @param (keyword) loader-id
  ;
  ; @return (component)
  [loader-id]
  (let [hidden? (a/subscribe [:x.app-components/infinite-loader-hidden? loader-id])]
       (fn []
           [:div.x-infinite-loader {:id (keyword/to-dom-value loader-id)}
                                   "Infinite loader"
                                   [observer loader-id (deref hidden?)]])))

(defn view
  ; @param (keyword) loader-id
  ; @param (function) callback
  ;
  ; @return (component)
  [loader-id callback]
  (let [observer-id (loader-id->observer-id loader-id)
        element-id  (keyword/to-dom-value   observer-id)]
       (lifecycles {:component-did-mount (fn [] (dom/setup-intersection-observer! element-id callback))
                    :reagent-render      (fn [] [infinite-loader loader-id])})))
