
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.07.26
; Description: Fájl mentése a kliens eszközére, data-url formátum használatával
; Version: v1.2.4
; Compatibility: x4.3.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.file-saver
    (:require [app-fruits.dom     :as dom]
              [mid-fruits.candy   :refer [param]]
              [mid-fruits.keyword :as keyword]
              [x.app-core.api     :as a]
              [x.app-elements.api :as elements]
              [x.app-tools.temporary-component
               :refer [append-temporary-component! remove-temporary-component!]]))



;; -- Descriptions ------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
;  A fájl mentésének folyamata a [:x.app-tools.file-saver/save-file! ...]
;  esemény meghívásával indítható.



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def DEFAULT-FILENAME "untitled.txt")



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- save-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [file-saver (dom/get-element-by-id "x-file-saver")]
       (.click file-saver)))



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- saver-props->save-as-data-url?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) saver-props
  ;  {:data-url (string)(opt)}
  ;
  ; @return (boolean)
  [{:keys [data-url]}]
  (some? data-url))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- saver-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) saver-props
  ;
  ; @return (map)
  ;  {:filename (string)}
  [saver-props]
  (merge {:filename DEFAULT-FILENAME}
         (param saver-props)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-tools.file-saver/save-file!
  ; A kliens eszközére menti a data-url tartalmú fájlt a filename
  ; paraméterként átadott néven.
  ;
  ; @param (keyword)(opt) saver-id
  ; @param (map) saver-props
  ;  {:data-url (string)(opt)
  ;    Only w/o {:uri ...}
  ;   :filename (string)(opt)
  ;    Default: DEFAULT-FILENAME
  ;   :uri (string)(opt)
  ;    Only w/o {:data-url ...}}
  ;
  ; @usage
  ;  [:x.app-tools.file-saver/save-file! {...}]
  ;
  ; @usage
  ;  [:x.app-tools.file-saver/save-file! :my-file-saver {...}]
  ;
  ; @usage
  ;  [:x.app-tools.file-saver/save-file! {:data-url "data:text/plain;charset=utf-8,..."}
  ;                                       :filename "my-file.edn"}]
  ;
  ; @usage
  ;  [:x.app-tools.file-saver/save-file! {:uri      "/images/my-image.jpg"}
  ;                                       :filename "my-image.jpg"}]
  (fn [_ event-vector]
      (let [saver-id    (a/event-vector->second-id   event-vector)
            saver-props (a/event-vector->first-props event-vector)
            saver-props (a/prot saver-props saver-props-prototype)]
           [:x.app-tools.file-saver/render-save-file-dialog! saver-id saver-props])))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- file-saver-cancel-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) saver-id
  ; @param (map) saver-props
  ;
  ; @return (component)
  [saver-id saver-props]
  [elements/button {:color    :default
                    :on-click [:x.app-ui/close-popup! saver-id]
                    :label    :cancel!
                    :variant  :transparent}])

(defn- file-saver-save-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) saver-id
  ; @param (map) saver-props
  ;
  ; @return (component)
  [saver-id saver-props]
  [elements/button {:on-click {:dispatch-n [[:x.app-tools.file-saver/->save-accepted saver-id saver-props]
                                            [:x.app-ui/close-popup! saver-id]]}
                    :label    :save!
                    :variant  :transparent}])

(defn- file-saver-label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) saver-id
  ; @param (map) saver-props
  ;
  ; @return (component)
  [saver-id saver-props]
  [elements/polarity {:start-content [file-saver-cancel-button saver-id saver-props]
                      :end-content   [file-saver-save-button   saver-id saver-props]}])

(defn- file-saver
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) saver-id
  ; @param (map) saver-props
  ;  {:data-url (string)(opt)
  ;   :filename (string)
  ;   :uri (string)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [data-url filename uri] :as saver-props}]
  [:a#x-file-saver (if (saver-props->save-as-data-url? saver-props)
                       {:download filename :href data-url}
                       {:download filename :href uri})])

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) content-props
  ;  {:filename (string)}
  ;
  ; @return (hiccup)
  [_ {:keys [filename]}]
  [:<> [elements/text {:content     :save-file?
                       :font-weight :bold}]
       [elements/row {:content [:<> [elements/icon {:icon :text_snippet}]
                                    [elements/separator {:size :s
                                                         :orientation :vertical}]
                                    [elements/text {:content     filename
                                                    :font-weight :bold}]]}]])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-handled-fx
  :x.app-tools.file-saver/->save-accepted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) saver-id
  ; @param (map) saver-props
  (fn [[saver-id saver-props]]
      (append-temporary-component! [file-saver saver-id saver-props]
                                   (param save-file!))
      (remove-temporary-component!)))

(a/reg-event-fx
  :x.app-tools.file-saver/render-save-file-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; Párbeszédablakot nyit meg a fájl mentésével kapcsolatban.
  ;
  ; @param (keyword) saver-id
  ; @param (map) saver-props
  (fn [_ [_ saver-id saver-props]]
      [:x.app-ui/add-popup!
       saver-id
       {:content       #'view
        :content-props saver-props
        :label-bar     {:content       #'file-saver-label-bar
                        :content-props saver-props}
        :layout        :boxed}]))
