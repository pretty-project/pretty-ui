
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.plugins.plugin-handler.core.helpers)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) component-key
  ;
  ; @example
  ;  (core.helpers/component-id :my-namespace/my-plugin :view)
  ;  =>
  ;  :my-namespace.my-plugin/view
  ;
  ; @return (keyword)
  [plugin-id component-key]
  ; XXX#5467
  (if-let [namespace (namespace plugin-id)]
          (keyword (str namespace "." (name plugin-id)) (name component-key))
          (keyword (str               (name plugin-id)) (name component-key))))

(defn default-data-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) data-key
  ;
  ; @example
  ;  (core.helpers/default-data-path :my-plugin :suggestions)
  ;  =>
  ;  [:plugins :plugin-handler/suggestions :my-plugin]
  ;
  ; @return (vector)
  [plugin-id data-key]
  [:plugins (keyword "plugin-handler" (name data-key)) plugin-id])
