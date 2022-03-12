
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.plugins.item-editor.core.helpers)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) component-key
  ;
  ; @example
  ;  (component-id :my-extension :my-type :view)
  ;  =>
  ;  :my-extension.my-type-editor/view
  ;
  ; @return (keyword)
  [extension-id item-namespace component-key]
  ; XXX#5467
  ; A [components/stated ...] komponens {:initializer [...]} és {:destructor [...]} eseményei
  ; egyedi azonosító hiányában a komponens újrarenderelésekor ismételten megtörténnének!
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-editor")
           (name component-key)))

  ;(keyword (namespace editor-id)
  ;         (str (name editor-id) "-"
  ;              (name component-key))))
