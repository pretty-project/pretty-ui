
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.04.20
; Description:
; Version: v0.4.6
; Compatibility: x4.4.9



;; -- How to comment? ---------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) direction
;  :ltr, :rtl
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
;  =>
;  [:a]
;
; @return (map)
;  Visszatérési értéke az a és b változó szorzata



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns project-name.module-name.file-name
    (:require [x.my-sample.api]
              [x.our-sample.api]
              [mid-fruits.candy  :refer [param return]]
              [mid-fruits.map    :as map]
              [mid-fruits.string :as string]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [x.your-sample.api :as your-sample]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.your-sample.api
(def do-something? your-sample/do-something?)
(def do-something! your-sample/do-something!)



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (boolean)
(def DISPLAY-SOMETHING? true)

; @constant (string)
(def ILLEGAL-UNMOUNTING-ERROR "Illegal onmounting error")



;; -- State -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (string)
(def color (atom "#fff"))

; @atom (boolean)
(def expanded? (atom false))

; @atom (map)
(def state (atom {:color     "#fff"
                  :expanded? false}))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-public-function
  ; @param (keyword) my-id
  ; @param (map) my-props
  ;
  ; @return (*)
  [my-id my-props]
  (get-in my-props [:get :something]))

(defn- my-private-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) my-id
  ; @param (map) my-props
  ;
  ; @return (*)
  [my-id my-props]
  (get-in my-props [:get :something :else]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-my-sample
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (get-in db [:sample-path]))

(defn- get-your-sample
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (get-in db [:sample-path]))

(reg-sub :my-namespace/get-your-sample get-your-sample)

(defn- get-fruits-in-vector
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (vector)
  ;  [(keyword) apple
  ;   (keyword) banana
  ;   (string) pineapple
  ;   (string) pear]
  [db _]
  [:apple :banana "pineapple" "pear"])



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- my-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) my-props
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :height (px)
  ;   :width (px)}
  [my-props]
  (merge {:color  :my-default-color
          :height 42
          :width  42}
         (param article-data)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-my-item!
  ; @param (item-path vector) item-path
  ; @param (*) item
  ;
  ; @return (map)
  [db [_ item-path item]]
  (assoc-in db item-path item))

(defn set-your-item!
  ; @param (item-path vector) item-path
  ; @param (*) item
  ;
  ; @return (map)
  [db [_ item-path item]]
  (assoc-in db item-path item))

(reg-event-db :my-namespace/set-your-item! set-your-item!)

(defn- backup-my-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) my-id
  ; @param (map) my-props
  ;
  ; @return (map)
  [db [_ my-id my-props]]
  (assoc-in db [:backup-items my-id] my-props))

(defn store-my-props!
  ; @param (keyword) my-id
  ; @param (map) my-props
  ;
  ; @usage
  ;  (r my-namespace/store-my-props! :my-item {...})
  ;
  ; @return (map)
  [db [_ my-id my-props]]
  (as-> db % (r backup-my-props! % my-id my-props)
             (assoc-in % [:items my-id] my-props)))



;; -- Coeffect events ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn inject-my-item!
  ; @param (item-path vector) item-path
  ; @param (*) item
  ;
  ; @return (map)
  [cofx [_ item-path item]]
  (assoc-in cofx item-path item))

(reg-cofx :my-namespace/inject-my-item! inject-my-item!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(reg-event-fx
  :my-namespace/do-something!
  ; @param (keyword) my-id
  ; @param (map) my-props
  (fn [_ [_ my-id my-props]]
      [:your-namespace/do-something-else! my-id my-props]))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- do-something!
  ; @param (map) pie-props
  ;  {:pie-color (keyword)
  ;   :pie-height (px)
  ;   :pie-width (px)
  ;   :slice-duration (ms)}
  ;
  ; @return (vector)
  [pie-props]
  (slice-the-pie! pie-props))

(reg-fx :my-namespace/do-something! do-something!)

(defn- try-something!
  ; @param (map) joint-props
  ;
  ; @return (nil)
  [joint-props]
  (roll-a-joint! joint-props))

(reg-handled-fx :my-namespace/try-something! try-something!)



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(reg-event-fx
  :my-namespace/->data-fetched
  {:dispatch-n [[:my-namespace/show-message!]
                [:my-namespace/store-data!]]})



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- submenu
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ;
  ; @return (hiccup)
  [_ {:keys [title]}]
  [:div#submenu title])

(defn- menu
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ;
  ; @return (hiccup)
  [menu-id menu-props]
  [:div#menu [submenu menu-id menu-props]])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-logout {:dispatch-n []}})
