
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.07.26
; Description: Fájl mentése a kliens eszközére
; Version: v1.4.6
; Compatibility: x4.4.6



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



;; -- Usage -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage Szöveg mentése fájlként a kliens eszközre:
;  (def text     "My text")
;  (def data-url (str "data:text/plain;charset=utf-8," text))
;  (a/dispatch [:tools/save-file! {:data-url data-url
;                                  :filename "My file.txt"}])
;
; @usage Távoli fájl mentése a kliens eszközre:
;  (a/dispatch [:tools/save-file! {:uri      "/images/my-image.jpg"
;                                  :filename "my-image.jpg"}])



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def DEFAULT-FILENAME "untitled.txt")



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- save-file-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [file-saver (dom/get-element-by-id "x-file-saver")]
       (.click file-saver)))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- saver-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) saver-props
  ;
  ; @return (map)
  ;  {:filename (string)}
  [saver-props]
  (merge {:filename DEFAULT-FILENAME}
         (param saver-props)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :tools/save-file!
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
  ;  [:tools/save-file! {...}]
  ;
  ; @usage
  ;  [:tools/save-file! :my-file-saver {...}]
  ;
  ; @usage
  ;  [:tools/save-file! {:data-url "data:text/plain;charset=utf-8,..."}
  ;                      :filename "my-file.edn"}]
  ;
  ; @usage
  ;  [:tools/save-file! {:uri      "/images/my-image.jpg"}
  ;                      :filename "my-image.jpg"}]
  (fn [_ event-vector]
      (let [saver-id    (a/event-vector->second-id   event-vector)
            saver-props (a/event-vector->first-props event-vector)
            saver-props (a/prot saver-props saver-props-prototype)]
           [:tools/render-save-file-dialog! saver-id saver-props])))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- file-saver
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) saver-id
  ; @param (map) saver-props
  ;  {:data-url (string)(opt)
  ;   :filename (string)
  ;   :uri (string)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [data-url filename uri] :as saver-props}]
  [:a#x-file-saver (if (some? data-url)
                       {:download filename :href data-url}
                       {:download filename :href uri})])



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- cancel-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) saver-props
  ;
  ; @return (component)
  [popup-id _]
  [elements/button ::cancel-button
                   {:on-click [:x.app-ui/close-popup! popup-id]
                    :preset   :cancel-button}])

(defn- save-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) saver-props
  ;
  ; @return (component)
  [popup-id saver-props]
  [elements/button ::save-button
                   {:on-click {:dispatch-n [[:tools/->save-file-accepted popup-id saver-props]
                                            [:x.app-ui/close-popup!            popup-id]]}
                    :preset   :save-button}])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) saver-props
  ;
  ; @return (component)
  [popup-id saver-props]
  [elements/polarity ::header
                     {:start-content [cancel-button popup-id saver-props]
                      :end-content   [save-button   popup-id saver-props]}])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) saver-props
  ;  {:filename (string)}
  ;
  ; @return (hiccup)
  [_ {:keys [filename]}]
  [:<> [elements/text {:content :save-file? :font-weight :bold}]
       [elements/row  {:content [:<> [elements/icon      {:icon :text_snippet}]
                                     [elements/separator {:size :s :orientation :vertical}]
                                     [elements/text      {:content filename :font-weight :bold}]]}]])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- ->save-file-accepted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) saver-id
  ; @param (map) saver-props
  [saver-id saver-props]
  (append-temporary-component! [file-saver saver-id saver-props]
                               (param save-file-f))
  (remove-temporary-component!))

(a/reg-handled-fx :tools/->save-file-accepted ->save-file-accepted)

(a/reg-event-fx
  :tools/render-save-file-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; Párbeszédablakot nyit meg a fájl mentésével kapcsolatban.
  ;
  ; @param (keyword) saver-id
  ; @param (map) saver-props
  (fn [_ [_ saver-id saver-props]]
      [:x.app-ui/add-popup! saver-id
                            {:content       #'body
                             :content-props saver-props
                             :label-bar     {:content       #'header
                                             :content-props saver-props}
                             :layout        :boxed}]))
