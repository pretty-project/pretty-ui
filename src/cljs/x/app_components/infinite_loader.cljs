
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
  ;  (loader-id->observer-id :my-loader)
  ;  => :my-loader--observer
  ;
  ; @return (keyword)
  [loader-id]
  (keyword/append loader-id :observer "--"))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- observer-hidden?
  ; @param (keyword) loader-id
  ;
  ; @return (boolean)
  [db [_ loader-id]]
  (let [visible? (get-in db (db/path ::infinite-loaders loader-id :observer-visible?))]
      ;(= visible? nil) = (= visible? true)
       (= visible? false)))

(a/reg-sub :x.app-components/observer-hidden? observer-hidden?)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn hide-observer!
  ; @param (keyword) loader-id
  ;
  ; @usage
  ;  (r components/hide-observer! db :my-loader)
  ;
  ; @return (map)
  [db [_ loader-id]]
  (assoc-in db (db/path ::infinite-loaders loader-id :observer-visible?)
               (param false)))

; @usage
;  [:x.app-components/hide-observer! :my-loader]
(a/reg-event-db :x.app-components/hide-observer! hide-observer!)

(defn show-observer!
  ; @param (keyword) loader-id
  ;
  ; @usage
  ;  (r components/show-observer! db :my-loader)
  ;
  ; @return (map)
  [db [_ loader-id]]
  (assoc-in db (db/path ::infinite-loaders loader-id :observer-visible?)
               (param true)))

; @usage
;  [:x.app-components/show-observer! :my-loader]
(a/reg-event-db :x.app-components/show-observer! show-observer!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-components/reload-infinite-loader!
  ; @param (keyword) loader-id
  (fn [{:keys [db]} [_ loader-id]]
       ; Az infinite-loader komponensben elhelyezett observer viewport-on kívülre
       ; helyezése, majd visszaállítása újra meghívja az infinite-loader komponens
       ; számára callback paraméterként átadott függvényt.
      {:db (r hide-observer! db loader-id)
       ; A túlságosan rövid ideig (pl.: 5ms) a viewport-on kívülre helyezett observer
       ; nem minden esetben hívja meg a callback függvényt.
       :dispatch-later [{:ms 50 :dispatch [:x.app-components/show-observer! loader-id]}]}))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- observer
  ; @param (keyword) loader-id
  ; @param (boolean) hidden?
  ;
  ; @return (component)
  [loader-id]
  (let [observer-id (loader-id->observer-id loader-id)
        hidden?     (a/subscribe [:x.app-components/observer-hidden? loader-id])]
       (fn [] [:div {:id    (keyword/to-dom-value observer-id)
                     :style (if (deref hidden?)
                                {:position :fixed :bottom "-100px"})}])))

(defn- infinite-loader
  ; @param (keyword) loader-id
  ;
  ; @return (component)
  [loader-id]
  [:div.x-infinite-loader {:id (keyword/to-dom-value loader-id)}
                          ;"Infinite loader"
                          [observer loader-id]])

(defn view
  ; @param (keyword)(opt) loader-id
  ; @param (map) loader-props
  ; {:on-viewport (metamorphic-event)}
  ;
  ; @return (component)
  ([loader-props]
   [view nil loader-props])

  ([loader-id {:keys [on-viewport] :as loader-props}]
   (let [loader-id    (a/id                   loader-id)
         observer-id  (loader-id->observer-id loader-id)
         element-id   (keyword/to-dom-value   observer-id)
         callback-f  #(a/dispatch             on-viewport)]
        (lifecycles {:component-did-mount (fn [] (dom/setup-intersection-observer! element-id callback-f))
                     :reagent-render      (fn [] [infinite-loader loader-id])}))))
