
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.01.15
; Description:
; Version: v0.3.2
; Compatibility: x4.2.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.engine)



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn renderer-id->partition-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @example
  ;  (renderer-id->partition-id :my-renderer)
  ;  => :x.app-ui.my-renderer/elements
  ;
  ; @return (keyword)
  [renderer-id]
  (keyword (str "x.app-ui." (name renderer-id))
           (str "elements")))

(defn renderer-id->dom-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @example
  ;  (renderer-id->dom-id :my-renderer)
  ;  => :x-app-my-renderer
  ;
  ; @return (keyword)
  [renderer-id]
  (keyword (str "x-app-" (name renderer-id))))
