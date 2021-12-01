
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.18
; Description:
; Version: v0.3.2
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.infinite-loader
    (:require [app-fruits.dom     :as dom]
              [app-fruits.reagent :refer [ratom lifecycles]]
              [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.random  :as random]
              [mid-fruits.vector  :as vector]
              [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]))



;; -- Usage -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  (ns my-namespace (:require [x.app-tools.api :as tools]))
;
;  [tools/infinite-loader {:on-viewport [:do-something!]}]

; @usage
;  (ns my-namespace (:require [x.app-tools.api :as tools]))
;
;  [tools/infinite-loader :my-infinite-loader {:on-viewport [:do-something!]}]
;
;  (a/dispatch [:tools/reload-infinite-loader! :my-infinite-loader])



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- loader-id->observer-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ;
  ; @example
  ;  (loader-id->observer-id :my-loader)
  ;  =>
  ;  :my-loader--observer
  ;
  ; @return (keyword)
  [loader-id]
  (keyword/append loader-id :observer "--"))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- infinite-observer-hidden?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ;
  ; @return (boolean)
  [db [_ loader-id]]
  (let [visible? (get-in db (db/path ::infinite-loaders loader-id :observer-visible?))]
      ;(= visible? nil) = (= visible? true)
       (= visible? false)))

(a/reg-sub ::infinite-observer-hidden? infinite-observer-hidden?)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn hide-infinite-observer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ;
  ; @return (map)
  [db [_ loader-id]]
  (assoc-in db (db/path ::infinite-loaders loader-id :observer-visible?) false))

(a/reg-event-db :tools/hide-infinite-observer! hide-infinite-observer!)

(defn show-infinite-observer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ;
  ; @return (map)
  [db [_ loader-id]]
  (assoc-in db (db/path ::infinite-loaders loader-id :observer-visible?) true))

(a/reg-event-db :tools/show-infinite-observer! show-infinite-observer!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :tools/reload-infinite-loader!
  ; @param (keyword) loader-id
  ;
  ; @usage
  ;  [:tools/reload-infinite-loader! :my-loader]
  (fn [{:keys [db]} [_ loader-id]]
       ; Az infinite-loader komponensben elhelyezett observer viewport-on kívülre
       ; helyezése, majd visszaállítása újra meghívja az infinite-loader komponens
       ; számára callback paraméterként átadott függvényt.
      {:db (r hide-infinite-observer! db loader-id)
       ; A túlságosan rövid ideig (pl.: 5ms) a viewport-on kívülre helyezett observer
       ; nem minden esetben hívja meg a callback függvényt.
       :dispatch-later [{:ms 50 :dispatch [:tools/show-infinite-observer! loader-id]}]}))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- infinite-observer
  ; @param (keyword) loader-id
  ; @param (boolean) hidden?
  ;
  ; @return (component)
  [loader-id]
  (let [observer-id (loader-id->observer-id loader-id)
        hidden?     (a/subscribe [::infinite-observer-hidden? loader-id])]
       (fn [] [:div {:id    (a/dom-value observer-id)
                     :style (if (deref hidden?)
                                {:position :fixed :bottom "-100px"})}])))

(defn- infinite-loader
  ; @param (keyword) loader-id
  ;
  ; @return (component)
  [loader-id]
  [:div.x-infinite-loader {:id (a/dom-value  loader-id)}
                          [infinite-observer loader-id]])

(defn component
  ; @param (keyword)(opt) loader-id
  ; @param (map) loader-props
  ; {:on-viewport (metamorphic-event)}
  ;
  ; @usage
  ;  [tools/infinite-loader {:on-viewport ...}]
  ;
  ; @return (component)
  ([loader-props]
   [component nil loader-props])

  ([loader-id {:keys [on-viewport] :as loader-props}]
   (let [loader-id    (a/id                   loader-id)
         observer-id  (loader-id->observer-id loader-id)
         element-id   (a/dom-value            observer-id)
         callback-f  #(a/dispatch             on-viewport)]
        (lifecycles {:component-did-mount (fn [] (dom/setup-intersection-observer! element-id callback-f))
                     :reagent-render      (fn [] [infinite-loader loader-id])}))))
