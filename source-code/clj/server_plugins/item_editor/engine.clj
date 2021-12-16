
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.4.6
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-editor.engine
    (:require [x.server-core.api :as a :refer [r]]
              [mid-plugins.item-editor.engine :as engine]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name multi-view?
;  TODO ...
;
; @name suggestion-keys
;  Az :item-editor/initialize! esemény számára {:suggestion-keys [...]} tulajdonságként
;  átadott kulcsokhoz tartozó értékeket az item-editor a kliens-oldali betöltődésekor
;  letölti a szerveren tárolt dokumentumokból, így azok a kliens-oldalon elérhetővé válnak
;  a dokumentum szerkesztéséhez használt mezők számára.



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-editor.engine
(def editor-uri              engine/editor-uri)
(def form-id                 engine/form-id)
(def item-id->new-item?      engine/item-id->new-item?)
(def item-id->form-label     engine/item-id->form-label)
(def item-id-key             engine/item-id-key)
(def request-id              engine/request-id)
(def mutation-name           engine/mutation-name)
(def resolver-id             engine/resolver-id)
(def route-id                engine/route-id)
(def extended-route-id       engine/extended-route-id)
(def route-template          engine/route-template)
(def extended-route-template engine/extended-route-template)
(def parent-uri              engine/parent-uri)
(def render-event            engine/render-event)
(def dialog-id               engine/dialog-id)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/initialize!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) editor-props
  ;  {:multi-view? (boolean)(opt)
  ;    Default: false
  ;   :suggestion-keys (keywords in vector)(opt)}
  ;
  ; @usage
  ;  [:item-editor/initialize! :my-extension :my-type]
  ;
  ; @usage
  ;  [:item-editor/initialize! :my-extension :my-type {...}]
  ;
  ; @usage
  ;  [:item-editor/initialize! :my-extension :my-type {:suggestion-keys [:color :city ...]}]
  (fn [_ [_ extension-id item-namespace {:keys [multi-view?] :as editor-props}]]
      {:dispatch [:router/add-route! (route-id extension-id item-namespace)
                                     {:route-template (route-template     extension-id item-namespace)
                                      :route-parent   (parent-uri         extension-id item-namespace)
                                      :client-event   [:item-editor/load! extension-id item-namespace editor-props]
                                      :restricted?    true}]
       :dispatch-if [(boolean multi-view?)
                     [:router/add-route! (extended-route-id extension-id item-namespace)
                                         {:route-template (extended-route-template extension-id item-namespace)
                                          :route-parent   (parent-uri              extension-id item-namespace)
                                          :client-event   [:item-editor/load!      extension-id item-namespace editor-props]
                                          :restricted?    true}]]}))
