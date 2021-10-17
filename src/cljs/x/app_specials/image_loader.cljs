
; WARNING! EXPIRED! DO NOT USE!



;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.23
; Description:
; Version: v0.4.8
; Compatibility:



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-specials.image-loader
    (:require [app-fruits.reagent :as reagent]
              [mid-fruits.css     :as css]
              [mid-fruits.map     :refer [dissoc-in]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.random  :as random]
              [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- container-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:container-class (string or vector)
  ;   :id (keyword)
  ;   :loaded? (boolean)
  ;   :uri (string)}
  ;
  ; @return (map)
  ;  {:class (string or vector)
  ;   :id (keyword)
  ;   :style (map or nil)}
  [{:keys [container-class id loaded? uri]}]
  {:class container-class
   :id (keyword/to-dom-value id)
   :style (if loaded? {:backgroundImage (css/url uri)})})



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ;
  ; @return (map)
  [db [_ loader-id]]
  (get-in db (db/path ::loaders loader-id)))

(a/reg-sub ::get-view-props get-view-props)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-initial-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) initial-props
  ;  {:id (keyword)}
  ;
  ; @return (map)
  [db [_ {:keys [id] :as initial-props}]]
  (assoc-in db (db/path ::loaders id) initial-props))

(defn- init-loader!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) initial-props
  ;  {:id (keyword)}
  ;
  ; @return (map)
  [db [_ initial-props]]
  (r store-initial-props! db initial-props))

(a/reg-event-db ::init-loader! init-loader!)

(defn- destruct-loader!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ;
  ; @return (map)
  [db [_ loader-id]]
  (dissoc-in db (db/path ::loaders loader-id)))

(a/reg-event-db ::destruct-loader! destruct-loader!)



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::->image-loaded
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  (fn [{:keys [db]} [_ loader-id]]
      {:x.app-db [[:set-item! (db/path ::loaders loader-id :loaded?)
                              (param true)]]}))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- loading-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:loaded? (boolean)}
  ;
  ; @return (hiccup)
  [{:keys [loaded?]}]
  (if-not loaded?
          [:div.x-column {:data-vertical-align (keyword/to-dom-value :center)}
            [:i.x-icon "image"]]))

(defn- control-image
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:id (keyword)
  ;   :uri (string)}
  ;
  ; @return (hiccup)
  [{:keys [id uri]}]
  [:img {:on-load #(a/dispatch [::->image-loaded id])
         :src uri
         :style {:display "none"}}])

(defn- image-loader
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [view-props]
  [:div (container-attributes view-props)
        [control-image        view-props]
        [loading-icon         view-props]])

(defn view
  ; WARNING!
  ; Ne változtasd az uri paraméter értékét! Cserélj inkább komponenst,
  ; mert ha új uri-t adsz meg ugyanannak a komponensnek, akkor a :loaded?
  ; értéke már az első pillanattól fogva TRUE lesz!
  ;
  ; @param (map) initial-props
  ;  {:container-class (string or vector)(opt)
  ;   :id (keyword)(opt)
  ;   :uri (string)}
  ;
  ; @return (component)
  [initial-props]
  (let [{:keys [id] :as initial-props} {}] ;(a/prot initial-props)]
       (reagent/lifecycles
        {:component-did-mount    #(a/dispatch [::init-loader! initial-props])
         :component-will-unmount #(a/dispatch [::destruct-loader! id])
         :reagent-render
         #(let [view-props (a/subscribe [::get-view-props id])]
               [image-loader (or @view-props initial-props)])})))
