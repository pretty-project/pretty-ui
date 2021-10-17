
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.xx.xx
; Description:
; Version: v0.1.0
; Compatibility: x3.9.8



;; -- How to comment? ---------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) direction
;  :ltr, :rtl
;  Default: :ltr
;
; @syntax
;  (vector/conj-item n x)
;
; @usage
;  (vector/conj-item [] :a)
;
; @example
;  (vector/conj-item [] :a)
;  => [:a]
;
; @return (map)
;  Visszatérési értéke az a és b változó szorzata



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns project-name.module-name.file-name
    (:require [x.another-sample]
              [x.sample]
              [x.mid-utils.map    :as map]
              [x.mid-utils.string :as string]
              [x.mid-utils.vector :as vector]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def do-something? namespace/do-something?)
(def do-something! namespace/do-something!)



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(def DISPLAY-SOMETHING?       true)
(def ILLEGAL-UNMOUNTING-ERROR "Illegal onmounting error")



;; -- State -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def color     (atom "#fff"))
(def expanded? (atom false))

(def state (atom {:color     "#fff"
                  :expanded? false}))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def db-path [:sometimes :db-paths :are :too :long])

(defn- local-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (nil)
  [])



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->path-param
  ; @param (map) request
  ; @param (keyword) param-id
  ;
  ; @return (*)
  [request param-id]
  (get-in request [:path-params param-id]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-sample
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (get-in db [:sample-path]))

(defn- get-another-sample
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (get-in db [:sample-path]))

(reg-sub ::get-another-sample get-another-sample)

(defn- get-fruits-in-vector
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (vector)
  ;  [(keyword) apple
  ;   (keyword) banana
  ;   (string) pineapple
  ;   (string) pear]
  [db _]
  [:apple :banana "pineapple" "pear"])



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def menu-data-prototype
     {:event   [:event-id]
      :label   "Label"
      :menu-id nil})

(defn- article-data-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) article-data
  ;
  ; @return (map)
  [article-data]
  (merge article-data
         {:article-id   nil
          :label        "Label"
          :subscription [:subscription-id]}))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-item!
  ; @param (vector) item-path
  ; @param (*) item
  ;
  ; @return (map)
  [db [_ item-path item]]
  (assoc-in db item-path item))

(reg-event-db :x.app-db/set-item! set-item!)

(defn- store-item-ids!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keywords in vector) item-ids
  ; 
  ; @return (map)
  [db [_ item-ids]]
  (assoc-in db [:item-ids] item-ids))

(defn store-item-props!
  ; @param (keyword) item-id
  ; @param (map) item-props
  ; @param (map)(optional) store-props
  ;  {:store-backup? (boolean)}
  ;
  ; @usage
  ;  (r store-item-props! :my-item-id {...})
  ;  (r store-item-props! :my-item-id {... 1} {... 2})
  ;
  ; @return (map)
  [db [_ item-id item-props {:keys [store-backup?]}]]
  (cond-> db store-backup? (assoc-in db [:backup-items item-id] item-props)
             :always       (assoc-in db [:items item-id] item-props)))



;; -- Coeffect events ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn inject-item!
  ; @param (vector) item-path
  ; @param (*) item
  ;
  ; @return (map)
  [cofx [_ item-path item]]
  (assoc-in cofx item-path item))

(reg-cofx ::inject-item! inject-item!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(reg-event-fx
  ::set-item!
  ; @param (vector) item-path
  ; @param (*) item
  (fn [_ [_ item-path item]]
      {:x.app-db [[:set! item-path item]]}))

(reg-event-fx
  ::do-something?!
  (fn [{:keys [db]} _]
      {:dispatch-if [(r do-something? db)
                     [::do-something!]]}))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(reg-fx
 ::do-something!
 #(do-something!))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(reg-event-fx
  ::->data-fetched
  (fn [_ _]
      {:dispatch-n [[::show-message!]
                    [::store-data!]]}))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- submenu
  ; @param (map) menu-data
  ;
  ; @return (hiccup)
  [{:keys [title]}]
  [:div#submenu title])

(defn- menu
  ; @param (map) menu-data
  ;
  ; @return (hiccup)
  [menu-data]
  [:div#menu [submenu menu-data]])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(reg-lifecycles
  ::lifecycles
  {:on-logout
   {:dispatch-n []}})
